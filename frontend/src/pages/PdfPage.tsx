import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";

const PdfPage = ({setPdfId,pdfId} : {setPdfId:Function,pdfId:number}) => {
    const [pdf,setPdf] = useState({data:new Uint8Array()})
    useEffect( () => {
      ouvrirPDF()
    }, [])
    const ouvrirPDF = async () => {
        console.log("pdf id : "+pdfId)
        const res = await fetch(`http://localhost:8080/offerPdf/${pdfId}`)
        console.log("ouvrirPDF")
        if(res.status == 200){
            const data = await res.json();
            await setPdf({data:new Uint8Array(data.pdf)})
            await console.log("data length : " + data.data.byteLength)
        }
    }
    const {Document, Page, pdfjs} = require('react-pdf');

    const [numPages, setNumPages] = useState(null);
    const [pageNumber, setPageNumber] = useState(1);

    const onDocumentLoadSuccess =async ({ numPages } : { numPages: any }) => {

        setNumPages(numPages);

    }


    return (
        <>
            <Link to="/" className="btn btn-primary my-3">Home</Link>
            <Document
                file={pdf.data}
                onLoadSuccess={onDocumentLoadSuccess}
                onLoadError = {console.error}
            >
                <Page pageNumber={pageNumber} width={600} />
            </Document>
            <p>
                Page {pageNumber} of {numPages}
            </p>
        </>

    );
}
export default PdfPage