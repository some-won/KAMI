package me.zeroeightsix.kami.gui.windows

import imgui.ImGui
import imgui.ImGui.dragFloat
import imgui.ImGui.dragInt
import imgui.ImGui.sameLine
import imgui.api.demoDebugInformations
import imgui.dsl.button
import imgui.dsl.checkbox
import imgui.dsl.collapsingHeader
import imgui.dsl.window
import me.zeroeightsix.kami.gui.Themes
import me.zeroeightsix.kami.gui.windows.modules.ModuleWindowsEditor
import me.zeroeightsix.kami.gui.windows.modules.Modules
import kotlin.reflect.KMutableProperty0

object KamiSettings {

    var modifiersEnabled = false
    var settingsWindowOpen = false
    var swapModuleListButtons = false
    var oldModuleEditMode = false
    var hideModuleDescriptions = false
    private var styleIdx = 0
    var borderOffset = 10f
    var rainbowSpeed = 32
    var rainbowSaturation = 1f
    var rainbowBrightness = 1f
    private var experimental = false
    var interactOutsideGUI = false

    private val themes = Themes.Variants.values().map { it.name.toLowerCase().capitalize() }

    operator fun invoke() {
        fun setting(label: String, checked: KMutableProperty0<Boolean>, description: String) {
            checkbox(label, checked) {}
            sameLine()
            demoDebugInformations.helpMarker(description)
        }

        if (settingsWindowOpen) {
            window("Settings", ::settingsWindowOpen) {
                setting(
                    "Experimental features",
                    ::experimental,
                    "Shows settings that are considered experimental or buggy"
                )
                collapsingHeader("Module windows") {
                    setting(
                        "Swap list buttons",
                        ::swapModuleListButtons,
                        "When enabled, right clicking modules will reveal their settings menu. Left clicking will toggle the module."
                    )
                    setting("Hide descriptions", ::hideModuleDescriptions, "Hide module descriptions when its settings are opened.")
                    setting("Old edit method", ::oldModuleEditMode,"Enable the old module edit method. A question mark will appear next to module descriptions that will allow you to merge or detach modules.")
                    button("Reset module windows") {
                        Modules.reset()
                    }
                    if (!ModuleWindowsEditor.open) {
                        sameLine()
                        button("Open module windows editor") {
                            ModuleWindowsEditor.open = true
                        }
                    }
                }

                collapsingHeader("GUI") {
                    if (ImGui.combo("Theme", ::styleIdx, themes)) {
                        Themes.Variants.values()[styleIdx].applyStyle()
                    }

                    if (dragInt("Rainbow speed", ::rainbowSpeed, vSpeed = 0.1F, vMin = 1, vMax = 128)) {
                        rainbowSpeed = rainbowSpeed.coerceAtLeast(1) // Do not let users custom edit this below 1
                    }
                    if (dragFloat("Rainbow saturation", ::rainbowSaturation, vSpeed = 0.01F, vMin = 0f, vMax = 1f)) {
                        rainbowSaturation = rainbowSaturation.coerceIn(0f, 1f)
                    }
                    if (dragFloat("Rainbow brightness", ::rainbowBrightness, vSpeed = 0.01F, vMin = 0f, vMax = 1f)) {
                        rainbowBrightness = rainbowBrightness.coerceIn(0f, 1f)
                    }

                    if (experimental) {
                        setting("Always interactable GUI", ::interactOutsideGUI, "Allows you to interact with the GUI at any time, e.g. when chat is opened, or the game is paused.")
                    }
                }

                collapsingHeader("Overlay") {
                    dragFloat("Border offset", ::borderOffset, vMin = 0f, vMax = 50f, format = "%.0f")
                }

                collapsingHeader("In-game") {
                    setting("Keybind modifiers", ::modifiersEnabled, "Allows the use of keybinds with modifiers: e.g. chaining CTRL, ALT and K.")
                }
            }
        }
    }

}