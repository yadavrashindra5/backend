const getAllUsers = async () => {
    const promise = new Promise((resolve, reject) => {
        resolve({ data: [{ name: "rashindra", address: "janakpur" }, { name: "rashindra", address: "janakpur" }, { name: "rashindra", address: "janakpur" }] })
    })
    return promise;
}



export default { getAllUsers };