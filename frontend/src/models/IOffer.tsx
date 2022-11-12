export default interface IOffer {
    id: string,
    nomDeCompagnie: string,
    position: string,
    heureParSemaine: string,
    pdf: number[],
    salaire: string,
    department: string,
    adresse: string,
    token: string,
    companyId: string
    dateStageDebut: string,
    dateStageFin: string,
}
