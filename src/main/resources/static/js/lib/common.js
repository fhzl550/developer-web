//****************************************************************************************************
//1. Description : AJAX 호출 (비동기)
//2. Parameters  : 타입, 매핑 Url, DB, 성공 메세지, 에러 메세지
//3. Return Type : void
//****************************************************************************************************
function doActionAjax(type, url, param, callBack, errorCallBack) {

    $.ajax({
        type : type,
        url : url,
        data : JSON.stringify(param),
        contentType : "application/json",
        success : callBack || function (response) {
            console.log(response)
            let message = "성공적으로 작업을 수행 하였습니다.";
            console.log(message);
        },
        error : errorCallBack || function (error) {
            console.log(error)
            let message = "작업을 실행 하였습니다. 관리자에게 문의 해주세요";
            console.log(message);
        }
    })
}
//****************************************************************************************************
//1. Description : 성공 메세지 callback 함수
//2. Parameters : Json 응답, 성공 메세지
//3. Return Type : void
//****************************************************************************************************
function callback(response, message) {
    console.log("server response : " + response);
    console.log("success message : " + message);
}
//****************************************************************************************************
//1. Description : 에러 메세지 callback 함수
//2. Parameters : Json 에러, 에러 메세지
//3. Return Type : void
//****************************************************************************************************
function errorCallback(error, message) {
    console.log("server response : " + error);
    console.log("error message : " + message);
}