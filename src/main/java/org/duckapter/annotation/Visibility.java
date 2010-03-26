package org.duckapter.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;

public enum Visibility {

	AT_LEAST {
		@Override
		protected boolean checkPackage(int mod) {
			return !Modifier.isPrivate(mod) && !Modifier.isProtected(mod);
		}

		@Override
		protected boolean checkPrivate(int mod) {
			return true;
		}

		@Override
		protected boolean checkProtected(int mod) {
			return !Modifier.isPrivate(mod);
		}

		@Override
		protected boolean checkPublic(int mod) {
			return Modifier.isPublic(mod);
		}
	},
	AT_MOST {
		@Override
		protected boolean checkPackage(int mod) {
			return !Modifier.isPublic(mod);
		}

		@Override
		protected boolean checkPrivate(int mod) {
			return Modifier.isPrivate(mod);
		}

		@Override
		protected boolean checkProtected(int mod) {
			return Modifier.isProtected(mod) || Modifier.isPrivate(mod);
		}

		@Override
		protected boolean checkPublic(int mod) {
			return true;
		}
	},
	EXACT {
		@Override
		protected boolean checkPackage(int mod) {
			return !Modifier.isPrivate(mod) && !Modifier.isPublic(mod)
					&& !Modifier.isProtected(mod);
		}

		@Override
		protected boolean checkPrivate(int mod) {
			return Modifier.isPrivate(mod);
		}

		@Override
		protected boolean checkProtected(int mod) {
			return Modifier.isProtected(mod);
		}

		@Override
		protected boolean checkPublic(int mod) {
			return Modifier.isPublic(mod);
		}

	};

	public boolean check(Annotation anno, int mod){
		if (anno == null || anno instanceof Public) {
			return checkPublic(mod);
		}
		if (anno instanceof Package) {
			return checkPackage(mod);
		}
		if (anno instanceof Protected) {
			return checkProtected(mod);
		}
		if (anno instanceof Private) {
			return checkPrivate(mod);
		}
		return false;
	}
	
	protected abstract boolean checkPrivate(int mod);

	protected abstract boolean checkProtected(int mod);

	protected abstract boolean checkPackage(int mod);

	protected abstract boolean checkPublic(int mod);
	
	
}
