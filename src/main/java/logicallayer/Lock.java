package logicallayer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Lock {

	private static class LockObj {
		private int lockedCount;

		public int getLockCount() {
			return lockedCount;
		}

		public synchronized void removeCount() {
			lockedCount--;
		}

		public synchronized void addCount() {
			lockedCount++;
		}

		@Override
		public String toString() {
			return String.valueOf(lockedCount);
		}
	}

	private static Map<Integer, LockObj> lockMap = new ConcurrentHashMap<Integer, LockObj>();

	public static LockObj lock(int accNo) {
		synchronized (lockMap) {
			LockObj lockObj = lockMap.computeIfAbsent(accNo, k -> new LockObj());
			lockObj.addCount();
			return lockObj;
		}
	}

	public static void unLock(int accNo) {
		LockObj lock = lockMap.get(accNo);
		if (lock != null) {
			lock.removeCount();
			if (lock.getLockCount() == 0) {
				synchronized (lockMap) {
					if (lock.getLockCount() == 0) {
						lockMap.remove(accNo);
					}
				}
			}
		}
	}

}