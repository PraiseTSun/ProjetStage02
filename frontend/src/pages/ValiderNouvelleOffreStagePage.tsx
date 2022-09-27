import React, {useEffect, useRef, useState} from "react";
import {Button, Container, OverlayTrigger, Popover, Table} from "react-bootstrap";
import { usePdf } from '@mikecousins/react-pdf';

const ValiderNouvelleOffreStagePage = ({fetchOffresAttendreValide, offresAttendreValide, valideOffre, deleteOffre} :
                                           {fetchOffresAttendreValide : Function, offresAttendreValide :Array<object>,
                                               valideOffre : Function, deleteOffre : Function}) => {
    useEffect(()=>{
        fetchOffresAttendreValide()
    })
    //source : https://stackoverflow.com/questions/54814373/how-to-use-react-pdf-library-with-typescript
    // https://stackoverflow.com/questions/65338762/react-pdf-from-bytearray-response
    const { Document, Page } = require('react-pdf');

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
                                  <OverlayTrigger
                                    trigger="click" key="left" placement="left" overlay={
                                      <Popover>
                                          <Popover.Body>
                                               <Document file={{data : offre.pdf}}></Document>
                                          </Popover.Body>
                                      </Popover>
                                  }>
                                     <a>{offre.pdf}</a>
                                  </OverlayTrigger>
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
