<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Gherkin Tags and Format Changelog

## [0.6.6]
- Сценарии общих шагов NESTED не получат Test IT теги.

## [0.6.5]
- Автоматическая генерация тега @Screenshot для сценариев скриншотного тестирования.
- Сценарии уже имеющие ExternalId не получат дублирующий тег.

## [0.6.3]
- Поддержка всех будущих версий IDE
- Исправление делиметра в DisplayName во избежание проблем с автоматической проверкой орфографии

## [0.6.2]
- Неразрывный пробел вместо подчеркивания в теге DisplayName

## [0.6.1] - 2024-10-03
- Исправлен баг форматирования Gherkin-таблицы с ?-знаком
- Исправлен баг добавляющий лишнюю пустую строку в конце фичи
- Автокоррекция имени функционала, в случае, если оно отличается от имени feature-файла

## [0.6.0] - 2024-10-02
- Заготовка плагина основанная на [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template) и [Gherkin Table formatter v1.1](https://github.com/artavd/gherkin-table-formatter-idea-plugin)
- Работоспособный плагин, нет функций конвертации в CSV, не отлажен

[0.6.3]: https://github.com/eilatkin/gherkin-tags-and-format-plugin/compare/v0.6.2...v0.6.3

[0.6.2]: https://github.com/eilatkin/gherkin-tags-and-format-plugin/compare/v0.6.1...v0.6.2

[0.6.1]: https://github.com/eilatkin/gherkin-tags-and-format-plugin/compare/v0.6.0...v0.6.1

[0.6.0]: https://github.com/eilatkin/gherkin-tags-and-format-plugin/commits/v0.6.0

[0.6.5]: https://github.com/eilatkin/gherkin-tags-and-format-plugin/compare/v0.6.3...v0.6.5

[0.6.6]: https://github.com/eilatkin/gherkin-tags-and-format-plugin/compare/v0.6.5...v0.6.5
