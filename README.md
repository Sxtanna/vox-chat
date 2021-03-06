<p align="center">
  <a href="https://github.com/Sxtanna/vox-chat/">
    <img src="https://i.imgur.com/P3SoS2f.png" alt="logo" width="800">
  </a>
</p>


<p align="center">
  <br />
  <strong>Modern Minecraft Chat Formatter</strong>
  <br />
  <a href="https://github.com/Sxtanna/vox-chat/issues">Report a Bug</a>
  ·
  <a href="https://github.com/Sxtanna/vox-chat/pulls">Send a Pull Request</a>
</p>

-----

<p align="center">Default Configuration</p>

```yaml
#
# Actions are path addressable, reusable component additions.
#
# =======================================
#             Hover Template
# hover:
#   first_name:
#     text: "Single Line"
#   other_name:
#     text:
#       - "multiple"
#       - "lines"
# =======================================
#
# =======================================
#             Click Template
# click:
#   first_name:
#     show_cmd: "/suggest this command"
#   other_name:
#     open_url: "https://www.google.com"
#   third_name:
#     exec_cmd: "/execute this command"
# =======================================


actions:
  hover:
    player_info:
      text:
        - "&8~ &f&l%player_name%"
        - ""
        - "&6Rank&8:&f&o %vault_prefix%&r"
        - "&9Store&8:&f&o https://your.server.store"
  click:
    message:
      show_cmd: "/msg %player_name% "
    open_store:
      open_url: "https://your.server.store"

#
# Formats can be assigned via a permission using their name
#
#
# =======================================
#             Format Template
# formats:
#   default: # permission would be voxchat.format.default
#     allows:
#       colors: false # whether the player can use color codes
#       format: false # whether the player can use format codes
#
#     format: "%player_name% > %message%"
# =======================================

formats:
  default:
    allows:
      colors: false
      format: false
    format: "[&f%player_name%&r](click.message) &8»&r %message%&r"
  donator:
    allows:
      colors: true
      format: false
    format: "[%vault_prefix%&r](hover.player_info,click.open_store) [&7%player_name%&r](click.message) &8»&r %message%&r"
```