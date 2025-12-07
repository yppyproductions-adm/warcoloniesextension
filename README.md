🧱 1. Visão Geral dos Sistemas
⭐ 1) Núcleo Estratégico: Dados do Mundo de Guerra

O mod mantém um conjunto de dados persistentes que representam o “mundo War Colonies”, mesmo quando nenhum jogador está online.

Estruturas principais

WarWorldData

lista global de colônias (players + NPCs)

vilas inimigas registradas

relações diplomáticas (amizade / neutro / inimigo)

caravanas em trânsito (Shipments)

tarefas de construção off-chunk

reivindicações de conquista

WarColony

dono atual (player ou facção NPC)

posição e raio de território

nível da colônia

produção abstrata (comida, madeira, pedra, ferro…)

população e força militar abstrata

WarPlayerProfile

colônias controladas

reputação

guerras ativas

Tudo isso existe sem depender de chunk carregado.

⚙️ 2) Economia Virtual e Produção Contínua

Cada colônia possui um sistema de produção e consumo calculado periodicamente:

produção acumulada desde o último tick estratégico

consumo de comida e manutenção

recursos abstratos sincronizados com o Warehouse real

possibilidade de exército crescer, colônia sobreviver a guerras, etc.

O sistema usa:

lastTickTime → calcula intervalo → aplica produção e consumo

🧱 3) Construções em Off (Builder Virtual)

Permite que obras continuem avançando mesmo com o player offline ou distante.

VirtualBuildingTask

prédio alvo e nível desejado

quantidade total de blocos

blocos restantes

recursos entregues / faltantes

velocidade de construção virtual (blocos/minuto)

Comportamento:

longe do player: simulação reduz blocos restantes

perto do player: o builder acelera até alcançar o progresso simulado
→ não teleporta pronta, avança “ao vivo” rapidamente

🐴 4) Mercado, Mercadores e Caravanas com Escolta

Sistema de logística inteligente entre colônias.

Novo prédio: Mercado

enviar recursos para outra colônia

definir itens e quantidades

escolher tipo de caravana

selecionar escolta de soldados

Shipment (caravana simulada)

origem, destino

carga

poder militar da escolta

velocidade

hora de partida e chegada simulada

estados: PENDING → IN_TRANSIT → ARRIVED / LOST / AMBUSHED

Funcionamento:

ao sair da zona carregada: NPCs desaparecem e a simulação assume

chance de emboscada baseada na rota

player pode interceptar, defender ou saquear

ao chegar: entrega no Warehouse

Se player estiver presente no momento crítico, tudo ocorre fisicamente.
Caso contrário, ocorre no sistema abstrato.

⚔️ 5) Guerra, Vilas Inimigas e Invasões

EnemyVillage

força militar

produção

estoque de recursos

hostilidade

InvasionManager

agenda ataques contra colônias

se player estiver perto → invasão física

se não → combate abstrato

Resultados possíveis:

prédios danificados

cidadãos mortos

recursos saqueados

moral alterada

👑 6) Diplomacia, PvP e Conquista de Colônias

Jogadores e facções podem ser:

aliados

neutros

inimigos

Conquista:

vilas inimigas podem tomar colônias

jogadores podem reivindicar colônias de outros players

Mecanismo de reivindicação:

ConquestClaim

colônia alvo

atacante

dono original

timer de 5 minutos

Se dono original clicar na prefeitura → cancela
Se ninguém cancelar → colônia muda de dono

🗺️ 7) Territórios e Regras de Combate

Integra com o sistema de proteção do MineColonies.

TerritoryManager

sabe qual colônia controla qual área

regras em guerra:

inimigos podem quebrar blocos

inimigos podem colocar blocos

prédios podem ser danificados

vilas inimigas têm territórios próprios

🎮 2. Estrutura Inicial Que Dá Para Gerar Com o Cursor (Lote 1)

O Cursor pode montar tudo isso em uma única grande geração, incluindo:

📁 Estrutura NeoForge

build.gradle

settings.gradle

mods.toml

classe principal WarColoniesMod

🧩 Pacotes
br.com.warcolonies.core
br.com.warcolonies.data
br.com.warcolonies.colony
br.com.warcolonies.logistics
br.com.warcolonies.warfare
br.com.warcolonies.building
br.com.warcolonies.territory
br.com.warcolonies.command
br.com.warcolonies.config

🧠 Modelos de dados

WarWorldData

WarColony

WarPlayerProfile

EnemyVillage

Shipment

EscortInfo

VirtualBuildingTask

ConquestClaim

🛠️ Managers

WarColonyManager

WarEconomyManager

BuildingSimulationManager

LogisticsManager

WarfareManager / InvasionManager

DiplomacyManager

TerritoryManager

⏱️ Tick Estratégico

Sistema que roda a cada X segundos para avançar:

economia

caravanas

construções

invasões

claims de conquista

🧪 Comandos de debug

/war debug colonies

/war debug shipments

/war debug claims

/war spawn enemyVillage

/war test invasion

🏪 Stub do Mercado

registro inicial do prédio

lógica interna de criação de Shipment

TODOs para futura GUI

