var httpRequest;
var checkoutBtn = document.getElementById("checkoutBtn");
var orderID = document.getElementById("orderID").innerText;

checkoutBtn.addEventListener("click", getCheckout);


function getCheckout() {
    httpRequest = new XMLHttpRequest();
    httpRequest.onreadystatechange = getSnippet;
    httpRequest.open('POST', '/checkout');
    httpRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    httpRequest.send("klarna_order_id=" + encodeURIComponent(orderID));
}
function getSnippet() {
    console.log("get");
    var snippetText;
    if (httpRequest.readyState === XMLHttpRequest.DONE) {
        if (httpRequest.status === 200) {
            snippetText = httpRequest.responseText;
            var checkoutContainer = document.getElementById('my-checkout-container');
            checkoutContainer.innerHTML = snippetText;
            var scriptsTags = checkoutContainer.getElementsByTagName('script');
            // This is necessary otherwise the scripts tags are not going to be evaluated
            for (var i = 0; i < scriptsTags.length; i++) {
                var parentNode = scriptsTags[i].parentNode;
                var newScriptTag = document.createElement('script');
                newScriptTag.type ='text/javascript';
                newScriptTag.text = scriptsTags[i].text;
                parentNode.removeChild(scriptsTags[i]);
                parentNode.appendChild(newScriptTag);
            }
        }
    }
}