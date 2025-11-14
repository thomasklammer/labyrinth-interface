const { JavaGenerator, JAVA_COMMON_PRESET } = require("@asyncapi/modelina");
const fs = require("fs");
const { Parser } = require("@asyncapi/parser");

async function main() {
  const yaml = fs.readFileSync("docs/GameServer_asyncapi.yml", "utf8");

  const parser = new Parser();
  const { document } = await parser.parse(yaml);

  const generator = new JavaGenerator({
    presets: [JAVA_COMMON_PRESET]
  });

  const models = await generator.generateCompleteModels(document, {
    packageName: "labyrinth.contracts.models",
  });


  if (!fs.existsSync("./models/async")) {
    fs.mkdirSync("./models/async");
  }

  for (const model of models) {
    const fileName = `./models/async/${model.modelName}.java`;
    fs.writeFileSync(fileName, model.result);
    console.log("Generated:", fileName);
  }
}

main().catch(console.error);
