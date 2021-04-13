const validations = {
    "code": code => code == /[A-Za-z0-9]+/.exec(code),
    "description": description => description == /.*[\w]+.*/.exec(description),
    "price": price => price == /[0-9]+[.]{0,1}[0-9]*/.exec(price),
    "quantity": quantity => quantity == /[0-9]+/.exec(quantity)
};

const errorMessages = {
    "code": "The code field is not valid",
    "description": "The description field is not valid",
    "price": "The price field is not valid",
    "quantity": "The quantity field is not valid"
};

const errorResponseMessages = response => {
    if(response.status === 400) {
        return "Error! There are receipts that depend on this product";
    } else {
        return "An error occurred while processing request";
    }
};

hideInvalidAlert();

function hideInvalidAlert() {
    setElementHidden('alertInvalid', true);
}

function setElementHidden(elementId, hidden) {
    document.getElementById(elementId).hidden = hidden;
}

function addProduct() {
    setModalSubmitButtonLabel('Add');
    setActionAndShowModal('add');
}

function setActionAndShowModal(action) {
    setElementById('action', action);
    $('#addNewProductModal').modal();
}

function setElementById(elementId, value) {
    document.getElementById(elementId).value = value;
}

function editProduct(code, description, price, quantity) {
    setElementById('oldCode', code);
    setElementById('code', code);
    setElementById('description', description);
    setElementById('price', price);
    setElementById('quantity', quantity);
    setModalSubmitButtonLabel('Edit');
    setActionAndShowModal('edit');
}

function setModalSubmitButtonLabel(label) {
    document.getElementById('modalSubmit').innerText = label;
}

function validateFieldsAndSubmit() {
    hideInvalidAlert();
    var invalidInputs = Object.keys(validations).filter(key => !validations[key](document.getElementById(key).value));
    if(invalidInputs.length > 0) {
        showInvalidAlert(errorMessages[invalidInputs[0]]);
    } else {
        submitInputForm();
    }
}

function submitInputForm() {
    document.getElementById('inputForm').submit();
}

function showInvalidAlert(alertContent) {
    document.getElementById('alertInvalid').innerHTML = '<strong>Error!</strong> ' + alertContent;
    setElementHidden('alertInvalid', false);
}

function deleteProduct(code) {
    $.ajax({
        type: "DELETE",
        url: "./delete-product?code=" + code,
        success: function(){
            location.href = "./products";
        },
        error: function(response) {
            alert(errorResponseMessages(response));
        }
    });
}