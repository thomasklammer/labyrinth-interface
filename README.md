# The Amazing Labyrinth - API Contracts

This repository contains the API specifications and generated models for "The Amazing Labyrinth". It defines the interfaces for game communication (real-time) and server management (REST).

## Structure

* `/docs` - Contains the API specifications (Single Source of Truth).
    * `GameServer_asyncapi.yml`: WebSocket protocol for the game flow.
    * `GameManagementServer_openapi.yml`: REST API for server discovery and lifecycle management.
* `/models/async` - Contains the Java models generated from the AsyncAPI (Jackson-annotated).
* `/management.api` - Implementation of the management server (see its own README).

## Specifications

### Game Interaction API (AsyncAPI)
Defines the event-driven real-time communication between client and game server via WebSockets.
* **Protocol:** WebSocket
* **Format:** JSON (using `type` as discriminator)
* **File:** `docs/GameServer_asyncapi.yml`

### Game Management API (OpenAPI)
Defines the REST interface for registering and discovering game servers.
* **Protocol:** HTTP REST
* **File:** `docs/GameManagementServer_openapi.yml`

## Generating Models for Async-Api

This module generates Java data models from the AsyncAPI specification using **Modelina** and the **AsyncAPI Parser**.

## Requirements
- Node.js ≥ 18
- npm

## Installation
Install the required npm dependencies:

```bash
npm install
```

This installs:

```bash
@asyncapi/modelina — Java model generator

@asyncapi/parser — AsyncAPI document parser
```

Run the generator script:
```bash
node models/generate-async-models.js
```

This script will:

- Load and parse the AsyncAPI file
- Convert all components.schemas into Java classes
- Write the generated files to:
```bash
models/async
```

Existing files in this folder will be overwritten.