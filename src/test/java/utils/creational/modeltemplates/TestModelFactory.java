package utils.creational.modeltemplates;

public class TestModelFactory {
	
	public static MinimalTestModelBuilder createMinimalModel() {
		MinimalTestModelBuilder minimalModel = new MinimalTestModelBuilder();
		minimalModel.initModel();
		return minimalModel;
	}
	
	public static LinearTestModelBuilder createLinearModel() {
		LinearTestModelBuilder linearModel = new LinearTestModelBuilder();
		linearModel.initModel();
		return linearModel;
	}
	
	public static OneLevelModelBuilder createOneLevelModel() {
		OneLevelModelBuilder oneLevelModel = new OneLevelModelBuilder();
		oneLevelModel.initModel();
		return oneLevelModel;
	}
	
	public static TwoLevelModelBuilder createTwoLevelModel() {
		TwoLevelModelBuilder twoLevelModel = new TwoLevelModelBuilder();
		twoLevelModel.initModel();
		return twoLevelModel;
	}
}

