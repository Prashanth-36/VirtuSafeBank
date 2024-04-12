package logicallayer;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

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

	private static ConcurrentHashMap<Integer, LockObj> lockMap = new ConcurrentHashMap<Integer, LockObj>();

	static {
		Thread gc = new Thread(() -> mapGc());
		gc.setDaemon(true);
		gc.start();
		System.out.println("gc started");
	}

	static LockObj lock(int accNo) {
		synchronized (lockMap) {
			LockObj lockObj = lockMap.computeIfAbsent(accNo, k -> new LockObj());
			lockObj.addCount();
			return lockObj;
		}
	}

	static void unLock(int accNo) {
		LockObj lock = lockMap.get(accNo);
		if (lock != null) {
			lock.removeCount();
		}
	}

	private static void mapGc() {
		while (true) {
			int size = lockMap.size();
			if (size > 0) {
				if (size > 1000) {
					Thread.currentThread().setPriority(6);
				} else {
					Thread.currentThread().setPriority(5);
				}
				synchronized (lockMap) {
					Iterator<Entry<Integer, LockObj>> iterator = lockMap.entrySet().iterator();
					while (iterator.hasNext()) {
						Entry<Integer, LockObj> entry = iterator.next();
						Integer key = entry.getKey();
						LockObj value = entry.getValue();
						if (value.getLockCount() == 0) {
							iterator.remove();
							System.out.println("Removed: " + key + " -> " + value);
						}
					}
				}
			} else {
				try {
					System.out.println("Locks holded" + lockMap);
					Thread.sleep(TimeUnit.HOURS.toMillis(1));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}