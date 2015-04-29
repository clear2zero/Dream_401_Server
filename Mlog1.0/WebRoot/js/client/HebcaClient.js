function HebcaClient()
{
    this.clientCtrl = null;
}

HebcaClient.prototype = {

    _GetClientCtrl: function() {
        if (this.clientCtrl)
            return this.clientCtrl;
        else {
            var certMgrObj;
            try {
                certMgrObj = new ActiveXObject("HebcaP11X.CertMgr");
            }
            catch (e) {
                throw Error("没有河北CA数字证书助手或浏览器阻止其运行");
            }

            certMgrObj.Licence = "aWViYx5oZWIcYWhlnWxhaJplY2FoZWJjoKw7gT6hg+O7EtJhfalPbDH9UlE=";
            this.clientCtrl = certMgrObj;
            return this.clientCtrl;
        }
    },


    Sign: function(source) {
        var c = this._GetClientCtrl().SelectSignCert();
        return c.SignText(source, 1);
    },

    GetSignCert: function() {
        return this._GetClientCtrl().SelectSignCert().GetCertB64();
    },

    GetCryptCert: function() {
        return this._GetClientCtrl().SelectEncryptCert().GetCertB64();
    },
    
    //向Key中写入文件
    //dataName：Key中数据的名称，应用自己命名，注意避免和其它应用重复就可以了
    //fileName:文件路径
    //isPrivate: 是否私有。如果设置为true，表示私有，则读取此数据时需要先登录Key。如设为false,则不需登录就可以读取
    WriteData:function(dataName,fileName,isPrivate){
    	  //读取文件,并转化为base64格式
    	  var mgr=this._GetClientCtrl();
    	  var fileDataB64=mgr.Util.ReadFileBase64(fileName);
    	  
    	  var device=mgr.SelectDevice();
    	  device.WriteDataB64(dataName,fileDataB64,isPrivate);
    },
    
    //从Key中读取数据
    ReadData:function(dataName){
    	var mgr=this._GetClientCtrl();
    	 var device=mgr.SelectDevice();
    	 return device.ReadDataB64(dataName);
    },
    
    DeleteData:function(dataName){
    	var mgr=this._GetClientCtrl();
       var device=mgr.SelectDevice();
    	 device.DeleteData(dataName);
    },
    
    
    WriteTempFile:function (fileName,base64Data) {
        var mgr=this._GetClientCtrl();
        //获取系统临时文件夹
        var tmpPath=mgr.Util.GetFolderPath(5);
        var file=tmpPath+fileName;
        mgr.Util.WriteFileBase64(file,base64Data);
        return file;
    }

};
