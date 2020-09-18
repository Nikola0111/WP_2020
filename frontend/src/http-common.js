import axios from "axios";


export default axios.create({
    baseURL: "/PocetniREST/rest",
    headers: {
        "Content-type": "application/json",
    }
});