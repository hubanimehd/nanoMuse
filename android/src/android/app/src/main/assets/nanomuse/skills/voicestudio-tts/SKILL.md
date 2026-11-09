---
name: voicestudio-tts
description: Generate Hindi voiceover with the VoiceStudio OmniVoice engine instead of a paid TTS service. Use when the user asks for voiceover, narration, Hindi VO, dubbing voice, or a TTS voice.
version: "1.0.0"
---

# VoiceStudio TTS

## Standing rules

- Use VoiceStudio for voiceovers. Do not switch back to a paid TTS service unless the user explicitly asks for it again, and never spend paid TTS credits by default.
- Never pay, upgrade, or send audio to a cloud TTS service for this workflow without the user's explicit yes.
- Nothing uploads or publishes without the user's explicit approval of the draft first.
- The Gemini pre-delivery review still applies before any delivery or upload. Switching the TTS source does NOT waive that gate.

## What VoiceStudio is

VoiceStudio is a free, local text-to-speech engine. Its default engine is OmniVoice, which supports Hindi plus hundreds of other languages, including zero-shot voice cloning from a short clean reference clip. The desktop app is not needed for a voiceover pipeline: the engine runs through a command line interface.

## How voiceover is requested

Generate line by line, never one giant call for a whole video. A short line takes minutes on CPU with no GPU, so long videos are built line by line and assembled afterwards.

Request format for each line:

- Text: the exact Hindi line to speak.
- Language: Hindi (default).
- Voice design (no reference audio): fixed attributes such as gender, age, and pitch, for example "Male, Young Adult, Moderate Pitch". Voice-design attributes are a fixed vocabulary, free-form prose outside them is ignored, and design is trained on English and Chinese, so Hindi results must be listened to and QC checked, not assumed.
- Voice cloning (with reference audio): a 3 to 10 second clean reference clip the user is authorised to use, plus its exact transcript. Use this when the user wants the SAME voice as an earlier video.

Example request:

```text
Text: "यह एक परीक्षण है।"
Language: Hindi
Voice: Male, Young Adult, Moderate Pitch
Output: hindi-test.wav
```

## QC before calling it done

- Check the output is real audio with a sane duration. An error file at the output path is not audio.
- Then run the standing pipeline: voiceover treatment, timeline matching, and the pre-delivery review before any delivery or upload.
- If a line sounds wrong, regenerate that line only. Do not stretch the video timeline to fit a bad take.

## Limits

- The voice will sound different from paid TTS voices used before. There are no paid-service voices here. If the user wants the SAME voice as an earlier paid-service video, say so and ask whether to clone from an authorised reference clip or accept the new VoiceStudio voice.
- Generation is CPU-only and slow. For long videos, generate in small batches only if memory allows. Do not run two generations at once by default.
- Never pay, upgrade, or send audio to a cloud TTS service for this workflow without the user's explicit yes.

## Limits on this phone

- **VoiceStudio cannot run on this phone.** The engine needs a desktop-class Python environment with a machine-learning runtime and model weights, plus the wrapper script from the VM. None of that exists in the phone's on-device sandbox.
- **What the phone agent can do:** prepare the voiceover request (line-by-line text, language, voice design or reference clip details), keep the QC checklist, and hand the generation job to a server, relay, or VM that runs VoiceStudio. The phone agent must not claim it generated audio it did not generate.
- **What the phone agent cannot do:** run the VoiceStudio CLI, download or load the model weights, or start the VoiceStudio backend API on the phone.
- If the phone has its own on-device text-to-speech, it may be used only as a rough preview, never as the final voiceover, unless the user explicitly approves that voice for the video.
