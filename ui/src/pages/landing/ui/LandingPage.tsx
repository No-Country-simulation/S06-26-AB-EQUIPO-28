import { useEffect, useState } from "react";
import { useLanguage } from "@/shared/lib/i18n";
import { Button } from "@/shared/ui/Button";
import { LandingMap } from "./LandingMap.tsx";
import heroImage from "@/assets/hero.png";

function Icon({
  name,
  size = 18,
  className,
  style,
}: { name: string; size?: number; className?: string; style?: React.CSSProperties }) {
  const strokeProps = {
    fill: "none",
    stroke: "currentColor",
    strokeWidth: 1.7,
    strokeLinecap: "round" as const,
    strokeLinejoin: "round" as const,
  };
  const sizeStyle: React.CSSProperties = {
    width: size,
    height: size,
    display: "block",
    flexShrink: 0,
  };

  switch (name) {
    case "mapa":
      return (
        <svg viewBox="0 0 20 20" style={{ ...sizeStyle, ...style }} className={className}>
          <path {...strokeProps} d="M3 5.5 8 3.5l4 2 5-2v11l-5 2-4-2-5 2v-11Z" />
          <path {...strokeProps} d="M8 3.5v11M12 5.5v11" />
        </svg>
      );
    case "doc":
      return (
        <svg viewBox="0 0 20 20" style={{ ...sizeStyle, ...style }} className={className}>
          <path {...strokeProps} d="M5 2.5h7l3 3v12H5v-15Z" />
          <path {...strokeProps} d="M7.5 9h5M7.5 12h5" />
        </svg>
      );
    case "datos":
      return (
        <svg viewBox="0 0 20 20" style={{ ...sizeStyle, ...style }} className={className}>
          <ellipse {...strokeProps} cx="10" cy="5" rx="6" ry="2.4" />
          <path {...strokeProps} d="M4 5v10c0 1.3 2.7 2.4 6 2.4s6-1.1 6-2.4V5" />
          <path {...strokeProps} d="M4 10c0 1.3 2.7 2.4 6 2.4s6-1.1 6-2.4" />
        </svg>
      );
    case "chat":
      return (
        <svg viewBox="0 0 20 20" style={{ ...sizeStyle, ...style }} className={className}>
          <path
            {...strokeProps}
            d="M17 9.5c0 3.3-3.1 6-7 6-.9 0-1.8-.14-2.6-.4L3 16.5l1.2-3.1A5.6 5.6 0 0 1 3 9.5c0-3.3 3.1-6 7-6s7 2.7 7 6Z"
          />
        </svg>
      );
    case "barras":
      return (
        <svg viewBox="0 0 20 20" style={{ ...sizeStyle, ...style }} className={className}>
          <path {...strokeProps} d="M4 16.5v-4M10 16.5v-9M16 16.5v-6" />
        </svg>
      );
    case "escudo":
      return (
        <svg viewBox="0 0 20 20" style={{ ...sizeStyle, ...style }} className={className}>
          <path
            {...strokeProps}
            d="M10 2.5 16 5v5c0 4-2.7 6.4-6 7.5C6.7 16.4 4 14 4 10V5l6-2.5Z"
          />
          <path {...strokeProps} d="m7.5 9.8 1.8 1.8 3.2-3.4" />
        </svg>
      );
    case "ayuda":
      return (
        <svg viewBox="0 0 20 20" style={{ ...sizeStyle, ...style }} className={className}>
          <circle {...strokeProps} cx="10" cy="10" r="7.5" />
          <path {...strokeProps} d="M10 7v4M10 13v.01" />
        </svg>
      );
    case "flecha":
      return (
        <svg viewBox="0 0 20 20" style={{ ...sizeStyle, ...style }} className={className}>
          <path {...strokeProps} d="M5 10h10M10 5l5 5-5 5" />
        </svg>
      );
    case "check":
      return (
        <svg viewBox="0 0 20 20" style={{ ...sizeStyle, ...style }} className={className}>
          <path {...strokeProps} d="M5 10l3 3 7-7" />
        </svg>
      );
    case "grafico":
      return (
        <svg viewBox="0 0 20 20" style={{ ...sizeStyle, ...style }} className={className}>
          <path {...strokeProps} d="M3 17v-10a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2v10" />
          <path {...strokeProps} d="M7 13l3-3 3 3 4-5" />
        </svg>
      );
    case "ubicacion":
      return (
        <svg viewBox="0 0 20 20" style={{ ...sizeStyle, ...style }} className={className}>
          <path
            {...strokeProps}
            d="M10 2C6.5 2 4 4.5 4 8c0 4.5 6 10 6 10s6-5.5 6-10c0-3.5-2.5-6-6-6z"
          />
          <circle {...strokeProps} cx="10" cy="8" r="2.5" fill="currentColor" stroke="none" />
        </svg>
      );
    case "personas":
      return (
        <svg viewBox="0 0 20 20" style={{ ...sizeStyle, ...style }} className={className}>
          <path {...strokeProps} d="M10 5a3 3 0 1 0 0 6 3 3 0 0 0 0-6z" />
          <path {...strokeProps} d="M18 17c0-3-2.5-5-6-5s-6 2-6 5" />
          <circle {...strokeProps} cx="6" cy="9" r="1.5" fill="currentColor" stroke="none" />
          <circle {...strokeProps} cx="14" cy="9" r="1.5" fill="currentColor" stroke="none" />
        </svg>
      );
    default:
      return (
        <svg viewBox="0 0 20 20" style={{ ...sizeStyle, ...style }} className={className}>
          <circle {...strokeProps} cx="10" cy="10" r="7.5" />
        </svg>
      );
  }
}

const FEATURES = [
  { icon: "grafico", key: "diagnosis" },
  { icon: "chat", key: "query" },
  { icon: "mapa", key: "map" },
  { icon: "personas", key: "vulnerable" },
  { icon: "doc", key: "methodology" },
  { icon: "check", key: "recommendations" },
];

function Hero({ t }: { t: (key: string) => string }) {
  const [visible, setVisible] = useState(false);
  const [statsVisible, setStatsVisible] = useState(false);

  useEffect(() => {
    const t1 = setTimeout(() => setVisible(true), 100);
    const t2 = setTimeout(() => setStatsVisible(true), 600);
    return () => {
      clearTimeout(t1);
      clearTimeout(t2);
    };
  }, []);

  return (
    <section
      className="relative min-h-screen flex items-center justify-center overflow-hidden bg-background"
      style={{ backgroundImage: `url(${heroImage})`, backgroundSize: "cover", backgroundPosition: "center 35%" }}
      aria-labelledby="hero-title"
    >
      <div className="absolute inset-0 bg-background/90" />
      <div className="absolute inset-0 bg-[linear-gradient(180deg,transparent_40%,var(--background)_100%)]" />

      <div className="relative z-10 w-full max-w-6xl px-6 py-20 mx-auto">
        <div
          className={`transition-all duration-1000 ease-out ${
            visible ? "opacity-100 translate-y-0" : "opacity-0 translate-y-8"
          }`}
        >
          <span className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-primary/10 text-primary text-sm font-medium border border-primary/20">
            <Icon name="escudo" size={14} />
            {t("landing.hero.badge")}
          </span>

          <h1
            id="hero-title"
            className="mt-6 text-4xl sm:text-5xl lg:text-6xl font-bold tracking-tight text-foreground leading-[1.1]"
          >
            {t("landing.hero.title")}
          </h1>

          <p className="mt-5 text-lg sm:text-xl text-muted-foreground max-w-2xl leading-relaxed">
            {t("landing.hero.description")}
          </p>

          <div className="mt-8 flex flex-wrap gap-3">
            <Button
              variant="default"
              size="lg"
              className="group h-11 px-6"
              onClick={() => document.getElementById("mapa")?.scrollIntoView({ behavior: "smooth" })}
            >
              <Icon name="mapa" size={18} className="mr-2 group-hover:translate-x-0.5 transition-transform" />
              {t("landing.hero.cta.primary")}
            </Button>
            <Button
              variant="outline"
              size="lg"
              className="h-11 px-6"
              onClick={() => document.getElementById("metodologia")?.scrollIntoView({ behavior: "smooth" })}
            >
              {t("landing.hero.cta.secondary")}
              <Icon name="flecha" size={16} className="ml-2" />
            </Button>
          </div>
        </div>

        <div
          className={`mt-12 grid grid-cols-2 sm:grid-cols-4 gap-6 transition-all duration-1000 ease-out ${
            statsVisible ? "opacity-100 translate-y-0" : "opacity-0 translate-y-6"
          }`}
          role="list"
          aria-label="Estadísticas clave"
        >
          <div
            key="regions"
            className="text-center p-4 rounded-xl bg-card border border-border/50 hover:border-primary/30 transition-colors duration-300"
            role="listitem"
          >
            <div className="text-3xl sm:text-4xl font-bold text-foreground tracking-tight">
              2.500+
            </div>
            <div className="mt-1 text-sm text-muted-foreground font-medium">{t("landing.stats.regions")}</div>
          </div>
          <div
            key="indicators"
            className="text-center p-4 rounded-xl bg-card border border-border/50 hover:border-primary/30 transition-colors duration-300"
            role="listitem"
          >
            <div className="text-3xl sm:text-4xl font-bold text-foreground tracking-tight">
              12
            </div>
            <div className="mt-1 text-sm text-muted-foreground font-medium">{t("landing.stats.indicators")}</div>
          </div>
          <div
            key="coverage"
            className="text-center p-4 rounded-xl bg-card border border-border/50 hover:border-primary/30 transition-colors duration-300"
            role="listitem"
          >
            <div className="text-3xl sm:text-4xl font-bold text-foreground tracking-tight">
              94%
            </div>
            <div className="mt-1 text-sm text-muted-foreground font-medium">{t("landing.stats.coverage")}</div>
          </div>
          <div
            key="realtime"
            className="text-center p-4 rounded-xl bg-card border border-border/50 hover:border-primary/30 transition-colors duration-300"
            role="listitem"
          >
            <div className="text-3xl sm:text-4xl font-bold text-foreground tracking-tight">
              Tiempo real
            </div>
            <div className="mt-1 text-sm text-muted-foreground font-medium">{t("landing.stats.realtime")}</div>
          </div>
        </div>
      </div>

      <div className="absolute bottom-8 left-1/2 -translate-x-1/2 animate-bounce-slow" aria-hidden="true">
        <Icon name="flecha" size={24} className="text-muted-foreground/50" style={{ transform: "rotate(180deg)" }} />
      </div>
    </section>
  );
}

function Features({ t }: { t: (key: string) => string }) {
  const [visibleItems, setVisibleItems] = useState<Set<number>>(new Set());

  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            const target = entry.target as HTMLElement;
            const index = Number(target.dataset.index);
            setVisibleItems((prev) => new Set(prev).add(index));
          }
        });
      },
      { threshold: 0.15, rootMargin: "0px 0px -50px 0px" }
    );

    const cards = document.querySelectorAll("[data-feature-card]");
    cards.forEach((card) => observer.observe(card));

    return () => observer.disconnect();
  }, []);

  return (
    <section id="caracteristicas" className="py-20 sm:py-28 px-6 bg-background" aria-labelledby="features-title">
      <div className="max-w-6xl mx-auto">
        <header className="text-center max-w-2xl mx-auto mb-16">
          <h2
            id="features-title"
            className="text-3xl sm:text-4xl font-bold tracking-tight text-foreground"
          >
            {t("landing.features.title")}
          </h2>
          <p className="mt-4 text-lg text-muted-foreground leading-relaxed">
            {t("landing.features.subtitle")}
          </p>
        </header>

        <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-6" role="list">
          {FEATURES.map((feature, index) => (
            <article
              key={feature.key}
              data-index={index}
              data-feature-card
              className={`group relative rounded-2xl border border-border/50 bg-card p-6 transition-all duration-500 hover:border-primary/40 hover:shadow-xl hover:shadow-primary/5 ${
                visibleItems.has(index)
                  ? "opacity-100 translate-y-0"
                  : "opacity-0 translate-y-6"
              }`}
              role="listitem"
              style={{ transitionDelay: `${index * 80}ms` }}
            >
              <div className="absolute inset-0 bg-linear-to-br from-primary/5 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500 rounded-2xl" />
              <div className="relative z-10">
                <div className="inline-flex h-11 w-11 items-center justify-center rounded-xl bg-primary/10 text-primary mb-4 group-hover:bg-primary/20 transition-colors">
                  <Icon name={feature.icon} size={22} />
                </div>
                <h3 className="text-lg font-semibold text-foreground mb-2">{t(`landing.feature.${feature.key}.title`)}</h3>
                <p className="text-muted-foreground leading-relaxed mb-3">{t(`landing.feature.${feature.key}.description`)}</p>
                <div className="flex items-center gap-1.5 text-xs text-primary font-medium">
                  <Icon name="flecha" size={12} />
                  <span>{t(`landing.feature.${feature.key}.detail`)}</span>
                </div>
              </div>
            </article>
          ))}
        </div>
      </div>
    </section>
  );
}

function HowItWorks({ t }: { t: (key: string) => string }) {
  const steps = [
    {
      number: "01",
      key: "step1",
      mapView: { center: [-56.16, -34.9] as [number, number], zoom: 9 },
    },
    {
      number: "02",
      key: "step2",
      mapView: { center: [-56.16, -34.9] as [number, number], zoom: 10.5 },
    },
    {
      number: "03",
      key: "step3",
      mapView: { center: [-56.18, -34.88] as [number, number], zoom: 11 },
    },
    {
      number: "04",
      key: "step4",
      mapView: { center: [-56.16, -34.9] as [number, number], zoom: 9 },
    },
  ];

  return (
    <section className="py-20 sm:py-28 px-6 bg-muted/30" aria-labelledby="how-title">
      <div className="max-w-6xl mx-auto">
        <header className="text-center max-w-2xl mx-auto mb-16">
          <h2 id="how-title" className="text-3xl sm:text-4xl font-bold tracking-tight text-foreground">
            {t("landing.how.title")}
          </h2>
          <p className="mt-4 text-lg text-muted-foreground leading-relaxed">
            {t("landing.how.subtitle")}
          </p>
        </header>

        <div className="relative">
          <div className="hidden lg:block absolute left-1/2 top-0 bottom-0 w-px bg-border/50 -translate-x-1/2" aria-hidden="true" />
          <div className="space-y-12 lg:space-y-16">
            {steps.map((step, index) => (
              <div
                key={step.number}
                className={`relative flex gap-6 lg:gap-8 ${
                  index % 2 === 0 ? "lg:flex-row" : "lg:flex-row-reverse"
                }`}
              >
                <div className="flex-1 lg:w-1/2 flex flex-col items-center lg:items-end text-right lg:pr-12">
                  <div className="mb-4 flex items-center justify-end lg:justify-end gap-3">
                    <span className="text-3xl font-bold text-primary/20 font-mono">{step.number}</span>
                    <div className="w-16 h-px bg-border" aria-hidden="true" />
                  </div>
                  <h3 className="text-xl font-semibold text-foreground mb-2">{t(`landing.how.${step.key}.title`)}</h3>
                  <p className="text-muted-foreground leading-relaxed">{t(`landing.how.${step.key}.description`)}</p>
                </div>
                <div className="relative shrink-0 lg:w-1/2">
                  <div
                    className={`relative aspect-square max-w-sm mx-auto rounded-2xl bg-card border border-border/50 overflow-hidden ${
                      index % 2 === 0 ? "lg:ml-auto" : "lg:mr-auto"
                    }`}
                  >
                    <LandingMap
                      initialView={step.mapView}
                      className="w-full h-full"
                      interactive={false}
                    />
                  </div>
                  <div className="absolute left-1/2 top-1/2 -translate-x-1/2 -translate-y-1/2 w-3 h-3 rounded-full bg-primary border-4 hidden lg:block" aria-hidden="true" />
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  );
}

function Sources({ t }: { t: (key: string) => string }) {
  const sources = [
    { key: "labor", icon: "check" },
    { key: "health", icon: "check" },
    { key: "ine", icon: "check" },
    { key: "telecom", icon: "check" },
    { key: "municipal", icon: "check" },
  ];

  return (
    <section id="metodologia" className="py-20 sm:py-28 px-6 bg-background" aria-labelledby="sources-title">
      <div className="max-w-6xl mx-auto">
        <header className="text-center max-w-2xl mx-auto mb-12">
          <h2 id="sources-title" className="text-3xl sm:text-4xl font-bold tracking-tight text-foreground">
            {t("landing.sources.title")}
          </h2>
          <p className="mt-4 text-lg text-muted-foreground leading-relaxed">
            {t("landing.sources.subtitle")}
          </p>
        </header>

        <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-4" role="list">
          {sources.map((source, index) => (
            <article
              key={source.key}
              className="group rounded-xl border border-border/50 bg-card p-5 transition-all duration-300 hover:border-primary/30 hover:shadow-lg hover:shadow-primary/5"
              role="listitem"
              style={{ animationDelay: `${index * 60}ms` }}
            >
              <h3 className="font-semibold text-foreground mb-1">{t(`landing.source.${source.key}`)}</h3>
              <p className="text-sm text-muted-foreground">{t(`landing.source.${source.key}.desc`)}</p>
              <div className="mt-4 pt-4 border-t border-border/50 flex items-center gap-2 text-xs text-primary font-medium opacity-0 group-hover:opacity-100 transition-opacity">
                <Icon name={source.icon} size={12} />
                <span>{t("landing.feature.methodology.detail")}</span>
              </div>
            </article>
          ))}
        </div>
      </div>
    </section>
  );
}

function CTA({ t }: { t: (key: string) => string }) {
  return (
    <section className="py-20 sm:py-28 px-6 bg-primary" aria-labelledby="cta-title">
      <div className="max-w-3xl mx-auto text-center">
        <h2 id="cta-title" className="text-3xl sm:text-4xl font-bold tracking-tight text-primary-foreground">
          {t("landing.cta.title")}
        </h2>
        <p className="mt-4 text-lg text-primary-foreground/80 leading-relaxed">
          {t("landing.cta.description")}
        </p>
        <div className="mt-8 flex flex-col sm:flex-row gap-4 justify-center">
          <Button
            variant="secondary"
            size="lg"
            className="w-full sm:w-auto h-11 px-8 bg-primary-foreground text-primary hover:bg-primary-foreground/90"
            onClick={() => document.getElementById("mapa")?.scrollIntoView({ behavior: "smooth" })}
          >
            <Icon name="mapa" size={18} className="mr-2" />
            {t("landing.cta.primary")}
          </Button>
          <Button
            variant="outline"
            size="lg"
            className="w-full sm:w-auto h-11 px-8 border-primary-foreground/30 text-primary-foreground hover:bg-primary-foreground/10"
            onClick={() => document.getElementById("metodologia")?.scrollIntoView({ behavior: "smooth" })}
          >
            {t("landing.cta.secondary")}
            <Icon name="flecha" size={16} className="ml-2" />
          </Button>
        </div>
      </div>
    </section>
  );
}

function Footer({ t }: { t: (key: string) => string }) {
  return (
    <footer className="border-t border-border/50 bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/80" role="contentinfo">
      <div className="max-w-6xl mx-auto px-6 py-8 flex flex-col sm:flex-row items-center justify-between gap-4">
        <div className="flex items-center gap-3">
          <span className="flex h-9 w-9 items-center justify-center rounded-xl bg-primary text-primary-foreground font-semibold font-mono text-sm" aria-hidden="true">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="w-5 h-5">
              <path d="M12 2C6.5 2 2 6.5 2 12s4.5 10 10 10 10-4.5 10-10S17.5 2 12 2z" />
              <path d="M12 6v6l4 2" />
              <circle cx="12" cy="12" r="2" fill="currentColor" stroke="none" />
            </svg>
          </span>
          <span className="text-sm font-semibold text-foreground">{t("landing.footer.brand")}</span>
          <span className="hidden md:inline-flex items-center px-2 py-0.5 text-[10px] font-medium text-primary-foreground bg-primary rounded">{t("landing.footer.tagline")}</span>
        </div>
        <div className="flex items-center gap-4 text-sm text-muted-foreground">
          <span className="flex items-center gap-1">
            <Icon name="escudo" size={12} />
            {t("landing.footer.privacy")}
          </span>
          <span className="font-mono text-xs">{t("landing.footer.version")}</span>
        </div>
        <nav className="flex items-center gap-4 text-sm" aria-label="Enlaces de pie de página">
          <a href="/metodologia" className="text-muted-foreground hover:text-foreground transition-colors">
            {t("landing.footer.methodology")}
          </a>
          <a href="/panel" className="text-muted-foreground hover:text-foreground transition-colors">
            {t("landing.footer.panel")}
          </a>
          <a href="#" className="text-muted-foreground hover:text-foreground transition-colors">
            {t("landing.footer.privacy_link")}
          </a>
          <a href="#" className="text-muted-foreground hover:text-foreground transition-colors">
            {t("landing.footer.contact")}
          </a>
        </nav>
      </div>
    </footer>
  );
}

export default function LandingPage() {
  const { t } = useLanguage();

  return (
    <>
      <style>{`
        @keyframes bounce-slow {
          0%, 100% { transform: translateY(0); }
          50% { transform: translateY(8px); }
        }
        .animate-bounce-slow { animation: bounce-slow 2.5s ease-in-out infinite; }
        @media (prefers-reduced-motion: reduce) {
          .animate-bounce-slow { animation: none; }
          * { animation-duration: 0.01ms !important; transition-duration: 0.01ms !important; }
        }
      `}</style>
      <Hero t={t} />
      <Features t={t} />
      <HowItWorks t={t} />
      <Sources t={t} />
      <CTA t={t} />
      <Footer t={t} />
    </>
  );
}