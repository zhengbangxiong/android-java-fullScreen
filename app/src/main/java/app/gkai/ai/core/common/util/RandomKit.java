package app.gkai.ai.core.common.util;

import java.security.SecureRandom;


public class RandomKit {



	SecureRandom random = new SecureRandom();

	public int nextInt(final int startInclusive, final int endExclusive) {
		if (startInclusive == endExclusive) {
			return startInclusive;
		}
		return startInclusive + random.nextInt(endExclusive - startInclusive);
	}

	/**
	 * 下一次随机
	 * @return
	 */
	public int nextTime(int max) {
		int randomInt = random.nextInt(max);
		return randomInt;
	}

	/**
	 * 第一次进入 随机生成购买数量
	 * @return
	 */
	public int randomBuyNum() {
		int buyNum = nextTime(3);
		return buyNum;
	}

	public int generate() {
//		int buyNum = randomBuyNum();
//		String nickName = "";
//		if (buyNum > 0) {
//			Map<String, Object> buyMap = new HashMap<>();
//			for (int i = 0; i < buyNum; i++) {
//				nickName = NickNameRandomKit.generateName();
//				buyMap.put("nick_name", nickName.replaceFirst("的", "***"));
//				buyMap.put("message", "刚刚购买了" + nextInt(1, 3) + "矿机");
//			}
//			System.err.println("本次显示 " + buyMap);
//		} else {
//			System.err.println("buyNum 小于等于0 忽略");
//		}
		int nextTime = nextTime(5);
		System.err.println("下一次执行 " + nextTime + "秒后");
		return nextTime;
	}

	public void schedule(RandomKit randomKit, int delayTime) {
//		scheduler.schedule(() -> {
			int nextTime = randomKit.generate();
			schedule(randomKit, nextTime);
//		}, delayTime, TimeUnit.SECONDS);

	}

//	public void shutdown(){
//        scheduler.shutdownNow();
//    }

	public static void main(String[] args) {
		RandomKit randomKit = new RandomKit();
		int delayTime = randomKit.nextTime(5);//第一次进入5秒内
		randomKit.schedule(randomKit, delayTime);
	}

}
