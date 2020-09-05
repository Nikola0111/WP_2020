import axios from "axios";

export default axios.create({
    baseURL: "http://localhost:8080/PocetniREST/rest",
    headers: {
        "Content-type": "application/json",
        "changeOrigin": true,
        "secure": false
    }
});