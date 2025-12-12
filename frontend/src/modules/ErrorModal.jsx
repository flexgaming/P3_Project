import { useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import { BsEye } from "react-icons/bs";

function ErrorModal({ error, buttonVariant = "primary" }) {
    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    return (
        <>
            <Button variant={buttonVariant} onClick={handleShow}>
                View <BsEye />
            </Button>
            <Modal show={show} onHide={handleClose} size="lg" centered>
                <Modal.Header closeButton>
                    <Modal.Title>Error Details</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <pre style={{ whiteSpace: "pre-wrap", wordBreak: "break-word" }}>{JSON.stringify(error, null, 2)}</pre>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose} style={{ color: "#ffffff" }}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default ErrorModal;
