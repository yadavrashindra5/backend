import userService from "../services/user.service.js"

const getUsers = async (req, res) => {
  const users = await userService.getAllUsers()
  res.json(users)
}

export default {getUsers};