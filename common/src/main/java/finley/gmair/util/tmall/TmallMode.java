package finley.gmair.util.tmall;

public enum TmallMode {

    auto("自动模式"),
    cold("制冷模式"),
    heat("制热模式"),
    ventilate("通风模式"),
    airsupply("送风模式"),
    dehumidification("除湿模式"),
    reading("阅读模式"),
    movie("影院模式"),
    sleep("睡眠模式"),
    live("生活模式"),
    manual("手动模式"),
    silent("静音模式"),
    energy("省电模式"),
    normalWind("正常风模式"),
    natureWind("自然风模式"),
    sleepWind("睡眠风模式"),
    quietWind("静音风模式"),
    comfortableWind("舒适风模式"),
    babyWind("宝宝风模式"),
    cottons("棉织物模式"),
    synthetics("化纤模式"),
    wool("羊毛模式"),
    hygiene("除菌模式"),
    drumClean("筒清洁模式"),
    silk("丝绸模式"),
    holiday("假日模式"),
    smart("智能模式"),
    music("音乐模式"),
    zeroGravity("零重力模式"),
    snoreStop("止鼾模式"),
    diffuse("多人模式"),
    swing("摇摆模式"),
    power("强效模式"),
    common("普通模式"),
    work("工作模式"),
    cool("速冷模式"),
    frozen("速冻模式"),
    microDry("微干模式"),
    fullDry("全干模式"),
    superDry("超干模式");

    private String mode;

    TmallMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }
}
