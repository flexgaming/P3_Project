package P3.Project.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;

public class TestResult implements TestWatcher {
		@Override
		public void testSuccessful(ExtensionContext context) {
			System.out.println("✅ Test succeeded: " + context.getDisplayName());
		}

		@Override
		public void testFailed(ExtensionContext context, Throwable cause) {
			System.out.println("❌ Test failed: " + context.getDisplayName());
			System.out.println("   Reason: " + cause.getMessage());
		}

		@Override
		public void testAborted(ExtensionContext context, Throwable cause) {
			System.out.println("⚠️ Test aborted: " + context.getDisplayName());
		}
        @Override
		public void testDisabled(ExtensionContext context, java.util.Optional<String> reason) {
			System.out.println("🚫 Test disabled: " + context.getDisplayName() + 
                                reason.map(r -> " (" + r + ")").orElse(""));
		}
	@AfterEach
	void AfterEach(TestInfo info) {
		System.out.println("Test completed: " + info.getDisplayName());
	}
}
