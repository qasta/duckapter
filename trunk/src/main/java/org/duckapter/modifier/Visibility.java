package org.duckapter.modifier;

import java.lang.reflect.Modifier;

public enum Visibility {

	AT_LEAST {
		@Override
		public boolean checkPackage(int mod) {
			return !Modifier.isPrivate(mod) && !Modifier.isProtected(mod);
		}

		@Override
		public boolean checkPrivate(int mod) {
			return true;
		}

		@Override
		public boolean checkProtected(int mod) {
			return !Modifier.isPrivate(mod);
		}

		@Override
		public boolean checkPublic(int mod) {
			return Modifier.isPublic(mod);
		}
	},
	AT_MOST {
		@Override
		public boolean checkPackage(int mod) {
			return !Modifier.isPublic(mod);
		}

		@Override
		public boolean checkPrivate(int mod) {
			return Modifier.isPrivate(mod);
		}

		@Override
		public boolean checkProtected(int mod) {
			return Modifier.isProtected(mod) || Modifier.isPrivate(mod);
		}

		@Override
		public boolean checkPublic(int mod) {
			return true;
		}
	},
	EXACT {
		@Override
		public boolean checkPackage(int mod) {
			return !Modifier.isPrivate(mod) && !Modifier.isPublic(mod)
					&& !Modifier.isProtected(mod);
		}

		@Override
		public boolean checkPrivate(int mod) {
			return Modifier.isPrivate(mod);
		}

		@Override
		public boolean checkProtected(int mod) {
			return Modifier.isProtected(mod);
		}

		@Override
		public boolean checkPublic(int mod) {
			return Modifier.isPublic(mod);
		}

	};

	public abstract boolean checkPrivate(int mod);

	public abstract boolean checkProtected(int mod);

	public abstract boolean checkPackage(int mod);

	public abstract boolean checkPublic(int mod);
}
