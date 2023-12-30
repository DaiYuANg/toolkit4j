/* (C)2023*/
package org.nectar.refined.error;

import lombok.*;

public class TryCatch<E extends Exception> {

	//	protected TryCatch(TryBuilder<E, ?, ?> b) {
	//		super(b);
	//	}
	//
	//	@Contract("_ -> new")
	//	public static <E extends Exception> @NotNull TryCatch<E> attempt(Runnable runnable) {
	//		try {
	//			runnable.run();
	//			return new TryCatch<>(null);
	//		} catch (Exception e) {
	//			@SuppressWarnings("unchecked")
	//			val exception = (E) e;
	//			return new TryCatch<>(exception);
	//		}
	//	}
}
