# TrucoScoreBoardCompose

Placar eletrônico para jogo de Truco desenvolvido para Android com Jetpack Compose.

## Descrição

Aplicativo para controlar a pontuação de duas equipes em uma partida de Truco. Conta com uma interface temática e moderna que acompanha o tema do sistema (claro/escuro) e as cores do wallpaper em dispositivos com Android 12 ou superior.

## Funcionalidades

- Placar individual para **Nós** e **Eles**
- Botão **"Ganhou! 🎉"** por equipe — ao apertar, os pontos da rodada vão para aquela equipe
- Valor da rodada começa em **1 ponto** e aumenta conforme o botão de truco é pressionado
- Sequência do botão de truco: **Truco! (1pt) → Seis! (3pt) → Nove! (6pt) → Doze! (9pt) → Máximo (12pt)**
- Aviso automático quando uma equipe entra na **mão de 11**
- Aviso de **fim de jogo** com exibição do vencedor ao atingir 12 pontos
- **Animação** de destaque ao pontuar
- Botão de **reiniciar** para zerar o placar e começar nova partida

## Interface

- Tema dinâmico que acompanha o tema do sistema (claro/escuro)
- Em Android 12+, as cores seguem o wallpaper do dispositivo (**Material You**)
- Visual temático de baralho/cartas
- Placar em destaque com cores distintas para cada equipe

## Especificações Técnicas

| Item | Versão |
|---|---|
| Android Studio | Panda 1 |
| Kotlin | 2.2.0 |
| Gradle | 9.2.1 |
| AGP | 9.0.1 |
| API mínima | 26 (Android 8.0 Oreo) |

## Pacote

`br.edu.ifsp.scl.sc3037291.trucoscoreboardcompose`

## Estrutura do Projeto

```
app/
├── src/
│   └── main/
│       ├── java/br/edu/ifsp/scl/sc3037291/trucoscoreboardcompose/
│       │   └── MainActivity.kt
│       └── res/
│           ├── mipmap/
│           │   └── ic_launcher (várias densidades)
│           └── values/
│               ├── strings.xml
│               └── themes.xml
```

## Como executar

1. Clone o repositório
2. Abra o projeto no **Android Studio Panda 1**
3. Aguarde a sincronização do Gradle
4. Execute em um emulador ou dispositivo físico com Android 8.0 ou superior
