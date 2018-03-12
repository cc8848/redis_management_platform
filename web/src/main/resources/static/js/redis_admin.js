//查询redis string
function find_string() {
    var requestMap = {};
    requestMap.typeFlag = "string";
    requestMap.key = $("#string_key").val();

    $.ajax({
        url: "http://localhost:9090/redis/redisChange",
        type: 'POST',
        data: {
            reqJsonString: JSON.stringify(requestMap),
        },
        dataType: 'json',
        success : function(data) {
            document.getElementById("string_value").innerHTML=data['string'];
        }
    })
}

//查询redis string
function find_list() {
    var requestMap = {};
    requestMap.typeFlag = "list";
    requestMap.key = $("#list_key").val();

    $.ajax({
        url: "http://localhost:9090/redis/redisChange",
        type: 'POST',
        data: {
            reqJsonString: JSON.stringify(requestMap),
        },
        dataType: 'json',
        success : function(data) {
            document.getElementById("list_value").innerHTML=data['list'];
        }
    })
}

//查询redis string
function find_hash() {
    var requestMap = {};
    requestMap.typeFlag = "hash";
    requestMap.key = $("#hash_key").val();
    requestMap.child_key = $("#key").val();

    $.ajax({
        url: "http://localhost:9090/redis/redisChange",
        type: 'POST',
        data: {
            reqJsonString: JSON.stringify(requestMap),
        },
        dataType: 'json',
        success : function(data) {
            document.getElementById("hash_value").innerHTML=data['hash'];
        }
    })
}