import { useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";

// ErrorModal component to display error details in a modal
function ErrorModal({ error, buttonText = "View", buttonVariant = "primary" }) {
    // State to control modal visibility
    const [show, setShow] = useState(false);

    // Handlers to open and close the modal
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    return (
        <>
            <Button variant={buttonVariant} onClick={handleShow}>
                {buttonText}
            </Button>
            {/* Modal to display error details */}
            <Modal show={show} onHide={handleClose} size="lg" centered>
                <Modal.Header closeButton>
                    <Modal.Title>Error Details</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {/* Display the error details in a formatted JSON */}
                    <pre style={{ whiteSpace: "pre-wrap", wordBreak: "break-word" }}>{JSON.stringify(error, null, 2)}</pre>
                </Modal.Body>
                <Modal.Footer>
                    {/* Button to close the modal */}
                    <Button variant="secondary" onClick={handleClose} style={{ color: "#ffffff" }}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default ErrorModal;
