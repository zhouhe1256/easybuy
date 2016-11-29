package com.gaiya.android.async;

public interface ICallbackExecutor {

	void run(ICallback callback, com.gaiya.android.async.Arguments arguments);

	public class CurrentThreadExecutor implements ICallbackExecutor {

		@Override
		public void run(ICallback callback, com.gaiya.android.async.Arguments arguments) {
			callback.call(arguments);
		}

	}
}
