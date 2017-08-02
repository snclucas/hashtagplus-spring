var stompClient = null;



function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);

        console.log('Connected: ' + frame);
        sendName();
        stompClient.subscribe('/topic/greetings', function (greeting) {
            console.log(greeting.body);
            constructMessageList(JSON.parse(greeting.body));
        });
        stompClient.subscribe('/topic/greetings2', function (greeting) {
            console.log(greeting.body);
            constructHashtagList(JSON.parse(greeting.body));
        });
    });

}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message.title + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
    $( "#tester" ).click(function() { askHashtags(); });
});


$( document ).ready(function() {
    connect();
    doSomething();
});

function constructMessageList(body) {
    var messages = body[0];
    var user = body[1];

    var contentLength = $('ul#message-list > li').length;
    //$('ul#message-list').empty();
    messages.forEach(function(message){
        var content = makeMessage(message, user);
        $('ul#message-list').append(content);
    });

}


function constructHashtagList(hashtagAggs) {
    var contentLength = $('ul#hashtag-list > li').length;
    hashtagAggs.forEach(function(hashtagAgg){
        $('ul#hashtag-list').append('<li><a href="#">'+hashtagAgg.hashtag + ' ('+hashtagAgg.count+')'  +'</a></li>');
    });

}


function doSomething() {

    var notificationsList =  $("[data-messages]");
    //find prototype
    var liPrototype = $notificationsList.find("[cm-notification-prototype]").clone();

}



function makeMessage(message, user) {

    var notificationsList =  $("[data-messages]");
    //find prototype
    var liPrototype = $notificationsList.find("[cm-notification-prototype]").clone();


return html;

}




function askHashtags() {
    console.log(JSON.stringify({'name': $("#name").val()}));
    stompClient.send("/app/hashtags", {}, JSON.stringify({"name": "fgdfg"}));

}

