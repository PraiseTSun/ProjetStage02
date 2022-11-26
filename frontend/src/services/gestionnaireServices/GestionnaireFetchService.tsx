export const deleteRemoveStudent = (id: string, token: String): Promise<Response> => {
    return fetch(`http://localhost:8080/removeStudent/${id}`,
        {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ token: token })
        });
}

export const deleteRemoveCompany = (id: string, token: String): Promise<Response> => {
    return fetch(`http://localhost:8080/removeCompany/${id}`,
        {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ token: token })
        });
}

export const putValidateStudent = (id: string, token: String): Promise<Response> => {
    return fetch("http://localhost:8080/validateStudent/" + id,
        {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ token: token })
        });
}

export const putValidateCompany = (id: string, token: String): Promise<Response> => {
    return fetch(`http://localhost:8080/validateCompany/${id}`,
        {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ token: token })
        });
}

export const putUnvalidatedOffers = (token: string): Promise<Response> => {
    return fetch("http://localhost:8080/unvalidatedOffers", {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ "token": token })
    });
}

export const putValidatedOffersByYear = (year: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/validatedOffers/" + year, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ "token": token })
    });
}

export const putOfferPdf = (offerId: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/offerPdf/" + offerId.toString(), {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ "token": token })
    });
}

export const putValidateOffer = (offerId: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/validateOffer/" + offerId.toString(), {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ "token": token })
    })
}

export const deleteRemoveOffer = (offerId: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/removeOffer/" + offerId.toString(), {
        method: "DELETE",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ "token": token })
    });
}

export const putUnvalidatedCvStudents = (token: string): Promise<Response> => {
    return fetch("http://localhost:8080/unvalidatedCvStudents", {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ "token": token })
    });
}

export const putStudentUnvalidatedCv = (studentId: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/studentCv/" + studentId, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ "token": token })
    });
}

export const putValidateCv = (studentId: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/validateCv/" + studentId.toString(), {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ "token": token })
    });
}

export const putRefuseCv = (studentId: string, token: string, refusalReason: string): Promise<Response> => {
    return fetch("http://localhost:8080/refuseCv/" + studentId.toString(), {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ "token": token, "refusalReason": refusalReason })
    });
}

export const putUnvalidatedCompanies = (token: string): Promise<Response> => {
    return fetch('http://localhost:8080/unvalidatedCompanies', {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ token: token })
    })
}

export const putUnvalidatedStudents = (token: string): Promise<Response> => {
    return fetch("http://localhost:8080/unvalidatedStudents", {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ token: token })
    })
}

export const putGetContracts = (token: string): Promise<Response> => {
    return fetch("http://localhost:8080/contractsToCreate", {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({token: token})
    })
}

export const postCreateStageContract = (studentId: string, offerId: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/createStageContract", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({token: token, offerId: offerId, studentId: studentId})
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

export const putEvaluationPdf = (contratId: number, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/getEvaluationPDF/millieuStage/" + contratId, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const putGetContractsPourMillieuStage = (token: string): Promise<Response> => {
    return fetch(`http://localhost:8080/getEvaluatedContracts/millieuStage`, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}
export const putInfoContratPourEvaluateStage = (contratId: number, token: string): Promise<Response> => {
    return fetch(`http://localhost:8080/evaluateStage/${contratId}/getInfo`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            "id": contratId,
            "token": token
        })
    });
}

export const postEvaluationStage = (contratId:number, token: string,
                                    tachesAnnonces: string,
                                    integration: string,
                                    tempsReelConsacre: string,
                                    environementTravail: string,
                                    climatTravail: string,
                                    milieuDeStage: string,
                                    heureTotalPremierMois: number,
                                    heureTotalDeuxiemeMois: number,
                                    heureTotalTroisiemeMois: number,
                                    communicationAvecSuperviser: string,
                                    equipementFourni: string,
                                    volumeDeTravail: string,
                                    commentaires: string,
                                    signature: string,
                                    dateSignature: string ): Promise<Response> => {
    return fetch(`http://localhost:8080/evaluateStage/${token}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            "contractId": contratId,
            "tachesAnnonces": tachesAnnonces,
            "integration": integration,
            "tempsReelConsacre": tempsReelConsacre,
            "environementTravail": environementTravail,
            "climatTravail": climatTravail,
            "milieuDeStage": milieuDeStage,
            "heureTotalPremierMois": heureTotalPremierMois,
            "heureTotalDeuxiemeMois": heureTotalDeuxiemeMois,
            "heureTotalTroisiemeMois": heureTotalTroisiemeMois,
            "communicationAvecSuperviser": communicationAvecSuperviser,
            "equipementFourni": equipementFourni,
            "volumeDeTravail": volumeDeTravail,
            "commentaires": commentaires,
            "signature": signature,
            "dateSignature":dateSignature

        })
    });
}

export const putGetContrats = (token: string): Promise<Response> => {
    return fetch(`http://localhost:8080/getContractsToEvaluate/millieuStage`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            "token": token
        })
    });
}
export const putEvaluationParEntreprisePdf = (contratId: number, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/getEvaluationPDF/etudiant/" + contratId, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const putGetContractsPourEvaluationParEntreprise = (token: string): Promise<Response> => {
    return fetch(`http://localhost:8080/getEvaluatedContracts/etudiants`, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}




export const putContracts = (token: string): Promise<Response> => {
    return fetch('http://localhost:8080/getGestionnaireContracts', {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({token: token})
    })
}

export const putSignatureContract = (token: string, gestionnaireId: number, contratId : number, signature : string): Promise<Response> => {

    return fetch(`http://localhost:8080/gestionnaireSignature`, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({token: token, userId: gestionnaireId, contractId: contratId,signature : signature})
    })
}

export const putGestionnaireNotification = (token: string): Promise<Response> => {
    return fetch(`http://localhost:8080/getGestionnaireNotification`, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}
