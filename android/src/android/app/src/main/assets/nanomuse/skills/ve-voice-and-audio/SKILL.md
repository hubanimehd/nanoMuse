---
name: ve-voice-and-audio
description: Voice treatment for videos: loudness normalization, muffled-voice diagnosis, EQ recipe, and fixing clips normalization made worse. Use when building a video's audio step or the voice sounds bad.
version: "1.0.0"
---

# Voice and audio

## Standing rules

- Nothing uploads or publishes without the user's explicit approval of the draft first.
- Never pay for anything, and never upgrade any service.

## Always, as the last audio step

Normalize loudness on every voice-track piece:

- Target loudness **-16 LKFS**, true peak ceiling **-2.0 dBTP**.
- On this phone, use the on-device Linux shell with ffmpeg loudnorm if ffmpeg is available in the sandbox, for example `loudnorm=I=-16:TP=-2.0:LRA=11`.
- Check the measured output lines after the run. If normalization fails or ffmpeg is not available, give the user these exact values to apply by hand in their editor: target loudness -16 LKFS, true peak -2.0 dBTP.

## After normalizing, check for side effects

- A **silent** clip (B-roll with no sound) gets boosted up to +30 dB and turns into hiss. Set it back to 0 dB.
- A B-roll clip with **music or radio** in it will fight the music bed: pull it down about 8 dB.

## "My voice sounds muffled / underwater"

Measure before touching anything:

1. **Phase check:** are left and right identical (true dual mono)? Compute L minus R. If two different mics are summed (camera mic plus lav), comb filtering causes exactly this sound: use one channel only.
2. **Spectrum:** compare band levels of a 20 second talking section: below 300 Hz, around 1 kHz, above 3 kHz, above 6 kHz. A clean voice has its 3 kHz and above band roughly 10 to 15 dB below the total. A measurement of about 22 dB below with most energy under 300 Hz means the mic was covered (under a jacket) or badly placed.

## Fix (mostly EQ)

- Voice isolation or noise cleanup: apply per talking clip only, and leave B-roll alone so its natural sound stays. The original recipe used DaVinci Resolve Voice Isolation at 50. On this phone there is no Resolve, so use the best noise reduction the on-device editor or ffmpeg offers, and keep it light.
- EQ recipe (apply to one clip, then copy the same EQ to the other talking clips, never the volume, so the normalize gains survive):
  - high-pass 80 Hz
  - minus 3 dB at 250 Hz, Q about 1.2 (boom)
  - plus 3 dB at 3.5 kHz, Q about 1 (presence)
  - plus 2 dB high shelf at 10 kHz (air)
- With ffmpeg, the same recipe is: `highpass=f=80, equalizer=f=250:width_type=q:width=1.2:g=-3, equalizer=f=3500:width_type=q:width=1:g=3, highshelf=f=10000:g=2`.
- If the fix needs a desktop editor UI, ask the user first. They may prefer to do it themselves.

## Prevention (tell the user once)

Clip the lav on the outside of clothing, 15 to 20 cm below the chin. In stereo-mode receivers with one transmitter the voice is on one channel only.

## Limits on this phone

- DaVinci Resolve Studio is not available on this phone. Every Resolve step in the original skill (Normalize Audio Levels, Voice Isolation, Inspector EQ, Paste Attributes) is replaced here by ffmpeg loudnorm and EQ filters in the on-device Linux shell, or by the user's own editor on their phone.
- If ffmpeg is not installed in the on-device sandbox, audio treatment cannot run on the phone and needs a server, relay, or VM. Report that plainly instead of claiming the audio was treated.
- Heavy renders and long loudness passes may be slow on the phone. Keep batches small and report measured loudness numbers, never a claim that it "sounds good" without measuring.
