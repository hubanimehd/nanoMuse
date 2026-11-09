package io.github.nanomuse.skills

import android.content.Context
import com.openminis.app.data.repository.SkillRepository

/** App-managed instructions remain readable and can still be disabled. */
object BundledSkills {
    const val CREATOR_VERSION = "2.1.0"

    fun creator(context: Context): String =
        context.assets.open("nanomuse/skill-creator.md").bufferedReader().use { it.readText() }

    /** Subfolder names under assets/nanomuse/skills/ (one per bundled skill), sorted for a stable install order. */
    fun list(context: Context): List<String> =
        runCatching { context.assets.list("nanomuse/skills")?.toList() ?: emptyList() }
            .getOrElse { emptyList() }
            .filter { it.isNotBlank() && !it.startsWith(".") }
            .sorted()

    /** SKILL.md text for one bundled skill folder, or null when it cannot be read. */
    fun read(context: Context, name: String): String? =
        runCatching {
            context.assets.open("nanomuse/skills/$name/SKILL.md").bufferedReader().use { it.readText() }
        }.getOrNull()

    fun canEdit(source: String): Boolean = source != "bundled"

    /** Preserve user-owned names and all installed state when refreshing bundled text. */
    fun creatorUpdate(
        current: SkillRepository.Skill,
        description: String,
        body: String,
        now: Long,
    ): SkillRepository.Skill? {
        if (!current.isReadOnly || current.version >= CREATOR_VERSION) return null
        return current.copy(
            description = description,
            version = CREATOR_VERSION,
            body = body,
            updatedAt = now,
        )
    }
}
