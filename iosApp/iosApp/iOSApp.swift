import SwiftUI
import IosArtifact

@main
struct iOSApp: App {
    var body: some Scene {
		WindowGroup {
            ContentView()
                .onOpenURL(perform: { url in
                    MainViewControllerKt.handleDeepLink(host: url.host, query: url.query)
                })
		}
	}
}
