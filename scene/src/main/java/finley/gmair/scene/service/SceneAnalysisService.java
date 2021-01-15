package finley.gmair.scene.service;

/**
 * @author : Lyy
 * @create : 2021-01-14 15:29
 * @description 场景数据分析接口
 **/
public interface SceneAnalysisService {

    // 所有用户创建的场景内，所有模块的分布情况（开关、风量等）
    void commandComponentDistribution();

    // 某用户创建的场景内，所有模块的分布情况（开关、风量等）
    void commandComponentDistribution(String consumerId);
}
