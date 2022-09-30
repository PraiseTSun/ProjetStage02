import React, {useEffect} from "react";
import {Button, Container, Table} from "react-bootstrap";


const ValiderNouvelleOffreStagePage = ({fetchOffresAttendreValide, offresAttendreValide, valideOffre, deleteOffre, ouvrirPDF, isOvrirPDF, pdfid} :
                                           {fetchOffresAttendreValide : Function, offresAttendreValide :Array<object>,
                                               valideOffre : Function, deleteOffre : Function, ouvrirPDF : Function, isOvrirPDF :boolean, pdfid : number}) => {

    useEffect(()=>{
       // fetchOffresAttendreValide()
    })

    if(isOvrirPDF){
        window.open(`/pdfs/${pdfid}`)
    }
    return (
         <Container className="justify-content-center">
             <h1 className="card-header text-center mb-2 p-5">Valider les nouveaux offres</h1>
             <Table striped bordered hover bgcolor="Azure">
                  <thead>
                      <tr className="text-uppercase">
                          <th>Nom De Compagnie</th>
                          <th>Départment / Position</th>
                          <th>Heure Par Semaine / Adresse</th>
                          <th>Pdf</th>
                          <th>Valide</th>
                          <th>Non Valide</th>
                      </tr>
                  </thead>
                  <tbody>
                      {offresAttendreValide.map((offre:any) => (
                          <tr key={offre.id}>
                              <td>{offre.nomDeCompagnie}</td>
                              <td>{offre.department}<br/>{offre.position}</td>
                              <td>Heure : {offre.heureParSemaine}<br/>{offre.adresse}</td>
                              <td>
                                  <Button  className="bg-warning" onClick={()=>ouvrirPDF(offre.id)}>Ouvrir PDF</Button>
                              </td>
                              <td><Button className="bg-success" onClick={()=>{valideOffre(offre.id)}}>Valide</Button></td>
                              <td><Button className="bg-danger" onClick={()=>{deleteOffre(offre.id)}}>Non Valide</Button></td>
                          </tr>
                      ))}
                  </tbody>
             </Table>
         </Container>
    );
}

export default ValiderNouvelleOffreStagePage;
