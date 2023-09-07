# Multi-lang

A simple Minecraft plugin that enables chat translations in real-time using the free MyMemory API. Ideal for small to mid-scale servers.

This plugin was coded in 30 minutes and serves as a straightforward yet practical tool for in-game language translation.

## Requirements
- Spigot/Bukkit server
- Java JDK 8 or above
- How to Install
- Build the plugin using your favorite Java build tool (e.g., Maven, Gradle).
- Place the generated .jar file into the plugins folder of your Spigot/Bukkit server.
- Restart the server to enable the plugin.


## How to Use
To translate a message, format it like so:
- <sourceLang> Your Message Here <targetLang>
For example, 
- <en> Hello <es> 

will translate "Hello" from English to Spanish for all players receiving the message.

## Code Explanation
The plugin mainly consists of two parts:

- onPlayerChat captures any player chat event and checks if the message has language tags (e.g., <en>).
- handleLangTag takes care of translating the message to the intended language.
- We utilize the MyMemory API for the translation. The API request is performed in the translate method.

## Troubleshooting
If a translation fails, the message is sent as-is, and the sender gets notified with a chat message indicating the failure.

## Made using Paper 1.18.2
