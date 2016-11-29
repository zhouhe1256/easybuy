package com.gaiya.android.async;

public class Deferred implements IPromise {

	private volatile com.gaiya.android.async.State state = com.gaiya.android.async.State.PENDING;
    private volatile com.gaiya.android.async.Arguments argumentsForState;

	private final com.gaiya.android.async.ICallbackExecutor callbackExecutor;

	private final com.gaiya.android.async.ICallback.Callbacks doneCallbacks = new com.gaiya.android.async.ICallback.Callbacks();
	private final com.gaiya.android.async.ICallback.Callbacks progressCallbacks = new com.gaiya.android.async.ICallback.Callbacks();
	private final com.gaiya.android.async.ICallback.Callbacks failCallbacks = new com.gaiya.android.async.ICallback.Callbacks();
	private final com.gaiya.android.async.ICallback.Callbacks alwaysCallbacks = new com.gaiya.android.async.ICallback.Callbacks();
	private final com.gaiya.android.async.ICallback.Callbacks cancelCallbacks = new com.gaiya.android.async.ICallback.Callbacks();
	
	private com.gaiya.android.async.ICancellable cancellable;

	public Deferred() {
		this(new com.gaiya.android.async.ICallbackExecutor.CurrentThreadExecutor());
	}

	public Deferred(com.gaiya.android.async.ICallbackExecutor callbackExecutor) {
		this.callbackExecutor = callbackExecutor;
	}
	
	public void setCancellable(com.gaiya.android.async.ICancellable cancellable) {
		this.cancellable = cancellable;
	}

	public com.gaiya.android.async.State getState() {
		return state;
	}

	private void execute(com.gaiya.android.async.Arguments arguments, com.gaiya.android.async.ICallback... callbacks) {
		for (com.gaiya.android.async.ICallback callback : callbacks) {
			callbackExecutor.run(callback, arguments);
		}
	}

	public synchronized void notify(com.gaiya.android.async.Arguments arguments) {
		execute(arguments, progressCallbacks);
	}

	public synchronized void resolved(com.gaiya.android.async.Arguments arguments) {
		if (state != com.gaiya.android.async.State.PENDING) {
			throw new IllegalStateException("state must be PENDING");
		}

		state = com.gaiya.android.async.State.RESOLVED;
        argumentsForState = arguments;
		execute(arguments, doneCallbacks, alwaysCallbacks);
	}

	public synchronized void reject(com.gaiya.android.async.Arguments arguments) {
		if (state != com.gaiya.android.async.State.PENDING) {
			throw new IllegalStateException("state must be PENDING");
		}

		state = com.gaiya.android.async.State.REJECTED;
        argumentsForState = arguments;
		execute(arguments, failCallbacks, alwaysCallbacks);
	}
	
	public synchronized void cancel(com.gaiya.android.async.Arguments arguments) {
		if (state != com.gaiya.android.async.State.PENDING) {
			//ignore
			return;
		}
		
		state = com.gaiya.android.async.State.CANCELED;
        argumentsForState = arguments;
		if (cancellable != null) {
			cancellable.doCancel(arguments);
		}
		execute(arguments, cancelCallbacks, alwaysCallbacks);
	}

	public synchronized Deferred done(com.gaiya.android.async.ICallback callback) {
		doneCallbacks.add(callback);

        if (state == com.gaiya.android.async.State.RESOLVED) {
            execute(argumentsForState, callback);
        }
		return this;
	}

	public synchronized Deferred progress(com.gaiya.android.async.ICallback callback) {
		progressCallbacks.add(callback);
		return this;
	}

	public synchronized Deferred fail(com.gaiya.android.async.ICallback callback) {
		failCallbacks.add(callback);

        if (state == com.gaiya.android.async.State.REJECTED) {
            execute(argumentsForState, callback);
        }
		return this;
	}

	public synchronized Deferred always(com.gaiya.android.async.ICallback callback) {
		alwaysCallbacks.add(callback);

        if (state != com.gaiya.android.async.State.PENDING) {
            execute(argumentsForState, callback);
        }
		return this;
	}
	
	public synchronized Deferred cancel(com.gaiya.android.async.ICallback callback) {
		cancelCallbacks.add(callback);

        if (state == com.gaiya.android.async.State.CANCELED) {
            execute(argumentsForState, callback);
        }
		return this;
	}

}
