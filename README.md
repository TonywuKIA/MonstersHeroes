# Monsters and Heroes (Console Java)

## How to run
```bash
javac -d out $(find src -name "*.java")
java -cp out com.monstersandheroes.Main
```
On Windows (PowerShell):
```powershell
& "C:\Program Files\Eclipse Adoptium\jdk-8.0.472.8-hotspot\bin\javac.exe" -d out (Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName })
& "C:\Program Files\Eclipse Adoptium\jdk-8.0.472.8-hotspot\bin\java.exe" -cp out com.monstersandheroes.Main
```

## Controls
- Movement: `W A S D`
- Show map: `R`
- Info: `I` (exploration & battle)
- Backpack (equip weapon/armor, use potion): `B`
- Market: `M` when on a market tile
- Escape battle: `E`
- Quit: `Q`

## Gameplay notes
- Map: 8x8 grid, tiles: `P` party, `M` market, `X` blocked, `·` common. Encounters: roll 1 on 1d5 when entering common tiles.
- Hero selection: choose class (Warrior/Sorcerer/Paladin) then pick heroes.
- Battle:
  - Hero actions: attack / cast spell / use potion / info / escape.
  - Damage (hero): `(STR + main/off-hand weapon dmg) * 0.05 - monster DEF*0.1`, min 1 after dodge.
  - Damage (monster): `monster base dmg * 0.5 * debuff - armor reduction`, min 0 after dodge.
  - Dodge: monsters use their dodge * 0.5; heroes use `min(0.8, AGI*0.001)`.
  - Weapons/armor have 3 uses; broken gear contributes no damage/reduction and can be repaired in the market for half price.
  - End of round recovery: HP/MP recover 20% up to `level * 100`.
- Market: buy/sell items (filtered by level), equip gear, use potions, repair broken gear (half price).
- Backpack (`B`): equip weapon/armor, use potions outside battle.

## Data files
Default heroes/monsters/items are loaded from `datas/` text files (`Warriors.txt`, `Paladins.txt`, `Sorcerers.txt`, `Dragons.txt`, `Exoskeletons.txt`, `Spirits.txt`, `Weaponry.txt`, `Armory.txt`, `Potions.txt`, `IceSpells.txt`, `FireSpells.txt`, `LightningSpells.txt`).

## Known limitations
- Durability is fixed (3 uses).
- No logging/saving (Not a reburic)
