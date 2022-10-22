export const putConfirmEmail = (userType: string, id: string): Promise<Response> => {
    return fetch("http://localhost:8080/confirmEmail/" + userType + "/" + id, {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
    });
}

export const putUserType = (userType: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/" + userType, {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({"token": token})
    })
}

export const postUserType = (userType: string, user: object): Promise<Response> => {
    return fetch("http://localhost:8080/create" + userType, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user)
    })
}

export const postUserTypeLogin = (userType: string, email: string, password: string): Promise<Response> => {
    return fetch("http://localhost:8080/" + userType + "/login", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({"email": email, "password": password})
    })
}