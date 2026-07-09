import { useLanguage, getDataSources, getAiMethodology } from "@/shared/lib/i18n";

export function MethodologyPage() {
  const { t, locale } = useLanguage();
  const sources = getDataSources(locale);
  const aiMethods = getAiMethodology(locale);

  return (
    <main className="mx-auto max-w-5xl px-6 py-12">
      <header className="mb-10">
        <h1 className="text-3xl font-bold text-foreground">{t("methodology.title")}</h1>
        <p className="mt-2 text-muted-foreground">{t("methodology.subtitle")}</p>
      </header>

      <div className="space-y-12">
        <section>
          <h2 className="mb-6 text-xl font-semibold text-foreground">{t("methodology.dataSources")}</h2>
          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
            {sources.map((source) => (
              <article key={source.name} className="rounded-xl border border-border bg-card p-5 ring-1 ring-foreground/5">
                <h3 className="mb-2 font-semibold text-foreground">{source.name}</h3>
                <p className="text-sm text-muted-foreground leading-relaxed">{source.description}</p>
              </article>
            ))}
          </div>
        </section>

        <section>
          <h2 className="mb-6 text-xl font-semibold text-foreground">{t("methodology.aiMethodology")}</h2>
          <div className="grid gap-4 sm:grid-cols-3">
            {aiMethods.map((method) => (
              <div key={method.title} className="rounded-xl border border-border bg-card p-5 ring-1 ring-foreground/5">
                <h3 className="mb-2 font-semibold text-foreground">{method.title}</h3>
                <p className="text-sm text-muted-foreground leading-relaxed">{method.description}</p>
              </div>
            ))}
          </div>
        </section>

        <section>
          <h2 className="mb-6 text-xl font-semibold text-foreground">{t("methodology.privacy")}</h2>
          <div className="grid gap-4 sm:grid-cols-2">
            <div className="rounded-xl border border-border bg-card p-5 ring-1 ring-foreground/5">
              <h3 className="mb-2 font-semibold text-foreground">{t("methodology.syntheticData")}</h3>
              <p className="text-sm text-muted-foreground leading-relaxed">{t("methodology.syntheticDataDesc")}</p>
            </div>
            <div className="rounded-xl border border-border bg-card p-5 ring-1 ring-foreground/5">
              <h3 className="mb-2 font-semibold text-foreground">{t("methodology.anonymization")}</h3>
              <p className="text-sm text-muted-foreground leading-relaxed">{t("methodology.anonymizationDesc")}</p>
            </div>
            <div className="rounded-xl border border-border bg-card p-5 ring-1 ring-foreground/5">
              <h3 className="mb-2 font-semibold text-foreground">{t("methodology.lgpd")}</h3>
              <p className="text-sm text-muted-foreground leading-relaxed">{t("methodology.lgpdDesc")}</p>
            </div>
            <div className="rounded-xl border border-border bg-card p-5 ring-1 ring-foreground/5">
              <h3 className="mb-2 font-semibold text-foreground">{t("methodology.security")}</h3>
              <p className="text-sm text-muted-foreground leading-relaxed">{t("methodology.securityDesc")}</p>
            </div>
          </div>
        </section>
      </div>
    </main>
  );
}
