function page_turning(page) {
    var requestMap = {};
    requestMap.page = page;

    var tempForm = document.createElement("form");
    tempForm.action = "http://localhost:9090/redis/stringData";
    tempForm.method = "post";
    tempForm.style.display = "none";
    var dict = document.createElement("input");
    dict.name = "page";
    dict.value = page;
    tempForm.appendChild(dict);
    document.body.appendChild(tempForm);
    tempForm.submit();
    return tempForm;
}