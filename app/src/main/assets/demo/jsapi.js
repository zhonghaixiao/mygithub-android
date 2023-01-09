class JsApi {

    constructor() {
        this.callHandlerMap = new Map()
        window.easyHiCallback = function (callbackName, paramsStr) {
            console.log(`easyHiCallback ${paramsStr.name}`)
            if(this.callHandlerMap[code]) {
                this.callHandlerMap[code]()
            }
        }
    }

    call(code, params, callback) {
        if (typeof easyHiObj !== "undefined") {
            this.callHandlerMap[code] = callback
            easyHiObj.callHandler(code, params)
        }
    }

}

window.jsApi = new JsApi()

export default new JsApi()
