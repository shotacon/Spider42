package top.shotacon.application.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtil {

	public static final ExecutorService executorService = Executors.newSingleThreadExecutor();
}
