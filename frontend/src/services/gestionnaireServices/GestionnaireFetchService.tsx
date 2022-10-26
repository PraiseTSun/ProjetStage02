export const deleteRemoveStudent = (id: string, token: String): Promise<Response> => {
    return fetch(`http://localhost:8080/removeStudent/${id}`,
        {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({token: token})
        });
}

export const deleteRemoveCompany = (id: string, token: String): Promise<Response> => {
    return fetch(`http://localhost:8080/removeCompany/${id}`,
        {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({token: token})
        });
}

export const putValidateStudent = (id: string, token: String): Promise<Response> => {
    return fetch("http://localhost:8080/validateStudent/" + id,
        {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({token: token})
        });
}

export const putValidateCompany = (id: string, token: String): Promise<Response> => {
    return fetch(`http://localhost:8080/validateCompany/${id}`,
        {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({token: token})
        });
}

export const putUnvalidatedOffers = (token: string): Promise<Response> => {
    return fetch("http://localhost:8080/unvalidatedOffers", {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const putValidatedOffersByYear = (year: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/validatedOffers/" + year, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const putOfferPdf = (offerId: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/offerPdf/" + offerId.toString(), {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const putValidateOffer = (offerId: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/validateOffer/" + offerId.toString(), {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    })
}

export const deleteRemoveOffer = (offerId: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/removeOffer/" + offerId.toString(), {
        method: "DELETE",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const putUnvalidatedCvStudents = (token: string): Promise<Response> => {
    return fetch("http://localhost:8080/unvalidatedCvStudents", {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const putStudentCv = (studentId: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/studentCv/" + studentId, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const putValidateCv = (studentId: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/validateCv/" + studentId.toString(), {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const putRefuseCv = (studentId: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/refuseCv/" + studentId.toString(), {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const putUnvalidatedCompanies = (token: string): Promise<Response> => {
    return fetch('http://localhost:8080/unvalidatedCompanies', {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({token: token})
    })
}

export const putUnvalidatedStudents = (token: string): Promise<Response> => {
    return fetch("http://localhost:8080/unvalidatedStudents", {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({token: token})
    })
}

export const postCreateGestionnaire = (firstName: string,
                                       lastName: string,
                                       email: string,
                                       password: string,
                                       token: string): Promise<Response> => {
    return fetch(`http://localhost:8080/createGestionnaire`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            "firstName": firstName,
            "lastName": lastName,
            "email": email,
            "password": password,
            "token": token
        })
    });
}

export const putcompanyContracts = (companyId: string, token: string): Promise<Response> => {
    return fetch(`http://localhost:8080/companyContracts/${companyId}`, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({token: token})
    })
}

