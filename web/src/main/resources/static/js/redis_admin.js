//查询redis string
function find_string() {
    var requestMap = {};
    requestMap.taskName = $("#taskName").val();
    requestMap.typeFlag = "string";
    requestMap.key = $("#string_key").val();

    $.ajax({
        url: "http://localhost:9090/redis/redisFind",
        type: 'POST',
        data: {
            reqJsonString: JSON.stringify(requestMap),
        },
        dataType: 'json',
        success: function (data) {
            if (data['returnCode'] === "0")
                $("#string_value").val(data['string']);
            else
                alert("fail: " + data['message']);
        }
    })
}

//查询redis list
function find_list() {
    var requestMap = {};
    requestMap.taskName = $("#taskName").val();
    requestMap.typeFlag = "list";
    requestMap.key = $("#list_key").val();

    $.ajax({
        url: "http://localhost:9090/redis/redisFind",
        type: 'POST',
        data: {
            reqJsonString: JSON.stringify(requestMap),
        },
        dataType: 'json',
        success: function (data) {
            $("#list_value").val(data['list']);
        }
    })
}

//查询redis hash
function find_hash() {
    var requestMap = {};
    requestMap.taskName = $("#taskName").val();
    requestMap.typeFlag = "hash";
    requestMap.key = $("#hash_key").val();
    requestMap.child_key = $("#key").val();

    $.ajax({
        url: "http://localhost:9090/redis/redisFind",
        type: 'POST',
        data: {
            reqJsonString: JSON.stringify(requestMap),
        },
        dataType: 'json',
        success: function (data) {
            $("#hash_value").val(data['hash']);
        }
    })
}

//change
function change_redis(typeFlag) {
    var requestMap = {};
    requestMap.taskName = $("#taskName").val();
    requestMap.typeFlag = typeFlag;
    requestMap.key = $("#" + typeFlag + "_key").val();
    requestMap.child_key = $("#key").val();
    requestMap.value = $("#" + typeFlag + "_value").val();

    $.ajax({
        url: "http://localhost:9090/redis/redisChange",
        type: 'POST',
        data: {
            reqJsonString: JSON.stringify(requestMap),
        },
        dataType: 'json',
        success: function (data) {
            alert(data["returnCode"] === "0" ? "success！" : "failed:" + data['message']);
        }
    })
}

//change
function delete_redis(typeFlag) {
    var requestMap = {};
    requestMap.taskName = $("#taskName").val();
    requestMap.typeFlag = typeFlag;
    requestMap.key = $("#" + typeFlag + "_key").val();
    requestMap.child_key = $("#key").val();

    $.ajax({
        url: "http://localhost:9090/redis/redisDelete",
        type: 'POST',
        data: {
            reqJsonString: JSON.stringify(requestMap),
        },
        dataType: 'json',
        success: function (data) {
            alert(data["returnCode"] === "0" ? "success！" : "failed:" + data['message']);
        }
    })
}

//EXPIRE
function redis_expire(typeFlag) {
    var requestMap = {};
    requestMap.taskName = $("#taskName").val();
    requestMap.valid_time = $("#valid_time").val();
    requestMap.key = $("#redis_key").val();

    $.ajax({
        url: "http://localhost:9090/redis/redisExpire",
        type: 'POST',
        data: {
            reqJsonString: JSON.stringify(requestMap),
        },
        dataType: 'json',
        success: function (data) {
            alert(data["returnCode"] === "0" ? "success！" : "failed:" + data['message']);
        }
    })
}