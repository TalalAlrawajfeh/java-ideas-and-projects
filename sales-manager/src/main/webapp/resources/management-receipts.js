const validations = {
    "code": code => code == /[A-Za-z0-9]+/.exec(code),
    "price": price => price == /[0-9]+[.]{0,1}[0-9]*/.exec(price),
    "quantity": quantity => quantity == /[0-9]+/.exec(quantity),
    "total": price => price == /[0-9]+[.]{0,1}[0-9]*/.exec(price)
};

const errorMessages = {
    "code": "The code field is not valid",
    "price": "The price field is not valid",
    "quantity": "The quantity field is not valid",
    "total": "The total field is not valid"
};

const errorResponseMessages = response => {
    if(response.status === 400) {
        return "Error! There are receipts that depend on this product";
    } else {
        return "An error occurred while processing request";
    }
};

hideInvalidAlert();

var productCode = document.getElementById('code').value;
if('' != productCode) {
    getProductPrice(productCode);
}

function hideInvalidAlert() {
    setElementHidden('alertInvalid', true);
}

function setElementHidden(elementId, hidden) {
    document.getElementById(elementId).hidden = hidden;
}

function addReceipt() {
    setModalSubmitButtonLabel('Add');
    setActionAndShowModal('add');
}

function setActionAndShowModal(action) {
    setElementById('action', action);
    $('#addReceiptModal').modal();
}

function setElementById(elementId, value) {
    document.getElementById(elementId).value = value;
}

function editReceipt(id, code, price, quantity, total) {
    setElementById('receiptId', id);
    setElementById('code', code);
    setElementById('price', price);
    setElementById('quantity', quantity);
    setElementById('total', total);
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

function areFieldsValid() {
    return Object.keys(validations).filter(key => !validations[key](document.getElementById(key).value)).length === 0;
}

function submitInputForm() {
    document.getElementById('inputForm').submit();
}

function showInvalidAlert(alertContent) {
    document.getElementById('alertInvalid').innerHTML = '<strong>Error!</strong> ' + alertContent;
    setElementHidden('alertInvalid', false);
}

function calculateTotal() {
    if(areFieldsValid) {
        var price = parseFloat(document.getElementById('price').value);
        var quantity = parseFloat(document.getElementById('quantity').value);
        var total = price * quantity;
        if(!isNaN(total) && isFinite(total)) {
            setElementById('total', total);
        }
    }
}

function deleteReceipt(id) {
    $.ajax({
        type: "DELETE",
        url: "./delete-receipt?receiptId=" + id,
        success: function(){
            location.href = "./receipts";
        },
        error: function(response) {
            alert(errorResponseMessages(response));
        }
    });
}

function getProductPrice(code) {
    $.ajax({
        type: "GET",
        dataType:"json",
        url: "./get-product?code=" + code,
        success: function(response){
            setElementById('price', response['price']);
        },
        error: function(response) {
            alert("An error occurred while processing request");
        }
    });
}