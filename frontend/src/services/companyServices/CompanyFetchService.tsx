import IOffer from "../../models/IOffer";

export const postCreateOffre = (offer: IOffer): Promise<Response> => {
    return fetch("http://localhost:8080/createOffre", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(offer)
    });
};


export const putcompanyContracts = (companyId: string, token: string, saison: string, year : number): Promise<Response> => {
    return fetch(`http://localhost:8080/companyContracts/${companyId}_${saison}-${year}`, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({token: token})
    })
}


export const putCompanySignatureContract = (token: string, companyId: string, contratId : number): Promise<Response> => {
    return fetch(`http://localhost:8080/companySignatureContract`, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({token: token, companyId: Number(companyId), stageContractId: contratId})
    })
}

