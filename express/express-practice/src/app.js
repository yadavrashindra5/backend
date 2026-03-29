import express from "express"
import router from "./routes/index.js";

const app = express()

app.use(express.json())

app.use("/api",router);

// Health check
app.get("/", (req, res) => {
  res.send("Server is running 🚀")
})

export default app