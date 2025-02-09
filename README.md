# CreepyBorder
Плагин на страшную границу мира. 

## Команды:
У плагина есть одна команда - команда перезагрузки конфигурации<br>
`creepyborder reload` или `cb reload` - перезагрузить конфигурацию.

<details>
  <summary>Особенности перезагрузки параметров биома</summary>
При перезагрузке биома, биом меняется только на стороне сервера

Для того, чтобы новые параметры биома были видны игроку, ему нужно перезайти на сервер.
</details>

## Конфигурация
Конфигурация плагина выглядит вот так
<details>
<summary> Пример конфигурации по умолчанию</summary>
```yaml
reload-permission: creepyborder.reload # пермишен на право перезагрузки плагина
immunity-permission: creepyborder.immunity # пермишен на иммунитет от эффектов страшной границы
handler-configs: # список хандлеров, накладываемых на игрока при приближении к границе
  -   name: Sound
      sounds:
        - AMBIENT_CAVE
      pitch: 1
      volume: 1
      period-in-tick: 300
      dist-to-border: 100.0
  -   name: Biome
      water-color: '#ff0000'
      sky-color: '#82004b'
      fog-color: '#8b0000'
      water-fog-color: '#8b0000'
      dist-to-border: 50.0
  -   name: MagmaGrab
      vector-length: 0.1
      dist-to-border: 20.0
  -   name: MagmaGrab
      vector-length: 0.7
      dist-to-border: 10.0
  -   name: PotionEffect
      potion-effect-datas:
        -   type: blindness
            amplifier: 0
        -   type: slowness
            amplifier: 8
        -   type: nausea
            amplifier: 2
      dist-to-border: 10.0
  -   name: TimedKill
      tick-to-kill: 200
      dist-to-border: 10.0
```
</details>

## Хандлеры страшной границы
Давайте рассмотрим более подробно последний пункт в конфиге

Каждый хандлер имеет минимум 2 параметра:
```yaml
name: # название хандлера
dist-to-border: # расстояние игрока до границы мира, при котором он начинает действовать (в блоках)
```

Для упрощения, хандлеры я буду называть эффектами страшной границы. Не путать с эффектами от зелий.

Всего на данный момент существует **5 эффектов страшной границы**:

### Sound
При использовании этого эффекта, игрока начинают проигроваться звуки после того, как он оказываются на определенном расстоянии от границы
```yaml
  -   name: Sound
      sounds:
        - AMBIENT_CAVE #список проигроиваемых звуков
      pitch: 1 
      volume: 1
      period-in-tick: 300 # Период в тиках. Каждые 300 тиков будут в случайном порядке проигрываться звук из списка
      dist-to-border: 100.0
```

### Biome
Этот эффект меняет биом вокруг игрока. Естественно, изменения биома видит только игрок. В самом мире биом не меняется
```yaml
-   name: Biome
    water-color: '#ff0000' # цвет воды
    sky-color: '#82004b' # цвет неба
    fog-color: '#8b0000' # цвет тумана
    water-fog-color: '#8b0000' # цвет под водой
    dist-to-border: 50.0
```

### MagmaGrab
Эффект притягивания игрока вниз, когда он в воде. Вне воды этот эффект не действует
```yaml
-   name: MagmaGrab
    vector-length: 0.1 # на сколько игрока притягивает вниз
    dist-to-border: 20.0
```

### PotionEffect
Накладывает эффекты зелий на игрока.
```yaml
-   name: PotionEffect
    potion-effect-datas: # Информация о накладываемых эффектах
    -   type: blindness # Тип эффекта
        amplifier: 0 # уровень (0 - это эффект I уровня. 1 - эффект II уровня)
    -   type: slowness
        amplifier: 8
    -   type: nausea
        amplifier: 2
    dist-to-border: 10.0
```
### Tick to kill
Эффект страшной границы, который убивает игрока через некоторое время.
```yaml
-   name: TimedKill
    tick-to-kill: 200 # Время до убийства игрока
    dist-to-border: 10.0
```

На данный момент это все эффекты. 

Некоторые эффекты можно повторять, например **PotionEffect, MagmaGrab, Sound**

Некоторые эффекты повторять бессмыслено, например **TickToKill**

Некоторые эффекты повторять нельзя. Их можно использовать только один раз, иначе могут возникнуть баги. Например **Biome**



