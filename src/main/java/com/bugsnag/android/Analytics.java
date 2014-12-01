package com.bugsnag.android;

/**
 * Session analytics tracking payload.
 *
 * This payload helps Bugsnag track app loads and provide you with "crashes
 * per session" metrics.
 */
class Analytics implements JsonStream.Streamable {
    private Configuration config;
    private Diagnostics diagnostics;
    private User user;

    Analytics(Configuration config, Diagnostics diagnostics, User user) {
        this.config = config;
        this.diagnostics = diagnostics;
        this.user = user;
    }

    public void toStream(JsonStream writer) {
        // Create a JSON stream and top-level object
        writer.beginObject();

            // Write the API key
            writer.name("apiKey").value(config.apiKey);

            // Write the notifier info
            writer.name("notifier").value(Notifier.getInstance());

            // Write diagnostics
            writer.name("app").value(diagnostics.getAppData());
            writer.name("device").value(diagnostics.getDeviceData());

            // Write user info
            writer.name("user").value(user);

        // End the main JSON object
        writer.endObject();
    }

    void deliver() throws HttpClient.NetworkException, HttpClient.BadResponseException {
        HttpClient.post(config.getAnalyticsEndpoint(), this);
    }
}
