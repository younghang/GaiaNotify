package com.young.gaianotify.RoomDataDefine;

import java.util.ArrayList;

/**
 * Created by young on 2016/2/17 0017.
 */
  class Permission
{
    public boolean fans  ; 
    public boolean guess  ;

    public boolean isFans() {
        return fans;
    }

    public void setFans(boolean fans) {
        this.fans = fans;
    }

    public boolean isGuess() {
        return guess;
    }

    public void setGuess(boolean guess) {
        this.guess = guess;
    }

    public boolean isReplay() {
        return replay;
    }

    public void setReplay(boolean replay) {
        this.replay = replay;
    }

    public boolean isMulti() {
        return multi;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    public boolean isShift() {
        return shift;
    }

    public void setShift(boolean shift) {
        this.shift = shift;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public boolean isFirework() {
        return firework;
    }

    public void setFirework(boolean firework) {
        this.firework = firework;
    }

    public boolean replay  ;
    public boolean multi  ; 
    public boolean shift  ; 
    public boolean video  ; 
    public boolean firework  ; 
}
class AnchorAttr {
    public Hots getHots() {
        return hots;
    }

    public void setHots(Hots hots) {
        this.hots = hots;
    }

    public Hots hots  ;
}

  class Flashvars
{
    public String Servers  ; 
    public ArrayList<Object> ServerIp  ; 
    public ArrayList<Object> ServerPort  ; 
    public ArrayList<Object> ChatRoomId  ; 
    public String VideoLevels  ; 
    public String cdns  ; 
    public int Status  ; 
    public int RoomId  ; 
    public boolean ComLayer  ;

    public String getServers() {
        return Servers;
    }

    public void setServers(String servers) {
        Servers = servers;
    }

    public ArrayList<Object> getServerIp() {
        return ServerIp;
    }

    public void setServerIp(ArrayList<Object> serverIp) {
        ServerIp = serverIp;
    }

    public ArrayList<Object> getServerPort() {
        return ServerPort;
    }

    public void setServerPort(ArrayList<Object> serverPort) {
        ServerPort = serverPort;
    }

    public ArrayList<Object> getChatRoomId() {
        return ChatRoomId;
    }

    public void setChatRoomId(ArrayList<Object> chatRoomId) {
        ChatRoomId = chatRoomId;
    }

    public String getVideoLevels() {
        return VideoLevels;
    }

    public void setVideoLevels(String videoLevels) {
        VideoLevels = videoLevels;
    }

    public String getCdns() {
        return cdns;
    }

    public void setCdns(String cdns) {
        this.cdns = cdns;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getRoomId() {
        return RoomId;
    }

    public void setRoomId(int roomId) {
        RoomId = roomId;
    }

    public boolean isComLayer() {
        return ComLayer;
    }

    public void setComLayer(boolean comLayer) {
        ComLayer = comLayer;
    }

    public String getVideoTitle() {
        return VideoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        VideoTitle = videoTitle;
    }

    public String getWebHost() {
        return WebHost;
    }

    public void setWebHost(String webHost) {
        WebHost = webHost;
    }

    public String getVideoType() {
        return VideoType;
    }

    public void setVideoType(String videoType) {
        VideoType = videoType;
    }

    public int getGameId() {
        return GameId;
    }

    public void setGameId(int gameId) {
        GameId = gameId;
    }

    public int getOnline() {
        return Online;
    }

    public void setOnline(int online) {
        Online = online;
    }

    public String getPv() {
        return pv;
    }

    public void setPv(String pv) {
        this.pv = pv;
    }

    public int getTuristRate() {
        return TuristRate;
    }

    public void setTuristRate(int turistRate) {
        TuristRate = turistRate;
    }

    public int getUseStIp() {
        return UseStIp;
    }

    public void setUseStIp(int useStIp) {
        UseStIp = useStIp;
    }

    public int getDlLogo() {
        return DlLogo;
    }

    public void setDlLogo(int dlLogo) {
        DlLogo = dlLogo;
    }

    public String VideoTitle  ;
    public String WebHost  ; 
    public String VideoType  ; 
    public int GameId  ; 
    public int Online  ; 
    public String pv  ; 
    public int TuristRate  ; 
    public int UseStIp  ; 
    public int DlLogo  ; 
}

  class Hots
{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getFight() {
        return fight;
    }

    public void setFight(String fight) {
        this.fight = fight;
    }

    public String getNowLevelStart() {
        return nowLevelStart;
    }

    public void setNowLevelStart(String nowLevelStart) {
        this.nowLevelStart = nowLevelStart;
    }

    public String getNextLevelFight() {
        return nextLevelFight;
    }

    public void setNextLevelFight(String nextLevelFight) {
        this.nextLevelFight = nextLevelFight;
    }

    public String name  ;
    public String level  ; 
    public String fight  ; 
    public String nowLevelStart  ; 
    public String nextLevelFight  ; 
}



class IsStar
{
    public int isWeek  ; 
    public int isMonth  ;

    public int getIsMonth() {
        return isMonth;
    }

    public void setIsMonth(int isMonth) {
        this.isMonth = isMonth;
    }

    public int getIsWeek() {
        return isWeek;
    }

    public void setIsWeek(int isWeek) {
        this.isWeek = isWeek;
    }
}


public class GaiaRoom
{
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DData getData() {
        return data;
    }

    public void setData(DData data) {
        this.data = data;
    }

    public int code  ;
    public String message  ; 
    public DData data  ; 
}