import { useNavigate } from "react-router-dom";
import { Button } from "@/shared/ui/Button";
import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
} from "@/shared/ui/Card";
import heroImage from "@/assets/hero.png";

function Icon({ name, size = 18 }) {
  const c = {
    fill: "none",
    stroke: "currentColor",
    strokeWidth: 1.7,
    strokeLinecap: "round",
    strokeLinejoin: "round",
  };
  const s = { width: size, height: size, display: "block", flexShrink: 0 };
  switch (name) {
    case "mapa":
      return (
        <svg viewBox="0 0 20 20" style={s}>
          <path {...c} d="M3 5.5 8 3.5l4 2 5-2v11l-5 2-4-2-5 2v-11Z" />
          <path {...c} d="M8 3.5v11M12 5.5v11" />
        </svg>
      );
    case "doc":
      return (
        <svg viewBox="0 0 20 20" style={s}>
          <path {...c} d="M5 2.5h7l3 3v12H5v-15Z" />
          <path {...c} d="M7.5 9h5M7.5 12h5" />
        </svg>
      );
    case "datos":
      return (
        <svg viewBox="0 0 20 20" style={s}>
          <ellipse {...c} cx="10" cy="5" rx="6" ry="2.4" />
          <path {...c} d="M4 5v10c0 1.3 2.7 2.4 6 2.4s6-1.1 6-2.4V5" />
          <path {...c} d="M4 10c0 1.3 2.7 2.4 6 2.4s6-1.1 6-2.4" />
        </svg>
      );
    case "chat":
      return (
        <svg viewBox="0 0 20 20" style={s}>
          <path {...c} d="M17 9.5c0 3.3-3.1 6-7 6-.9 0-1.8-.14-2.6-.4L3 16.5l1.2-3.1A5.6 5.6 0 0 1 3 9.5c0-3.3 3.1-6 7-6s7 2.7 7 6Z" />
        </svg>
      );
    case "barras":
      return (
        <svg viewBox="0 0 20 20" style={s}>
          <path {...c} d="M4 16.5v-4M10 16.5v-9M16 16.5v-6" />
        </svg>
      );
    case "escudo":
      return (
        <svg viewBox="0 0 20 20" style={s}>
          <path {...c} d="M10 2.5 16 5v5c0 4-2.7 6.4-6 7.5C6.7 16.4 4 14 4 10V5l6-2.5Z" />
          <path {...c} d="m7.5 9.8 1.8 1.8 3.2-3.4" />
        </svg>
      );
    default:
      return (
        <svg viewBox="0 0 20 20" style={s}>
          <circle {...c} cx="10" cy="10" r="7.5" />
          <path {...c} d="M8 8a2 2 0 1 1 2.8 1.85c-.55.24-.8.65-.8 1.15v.3" />
          <path stroke="none" fill="currentColor" d="M10 14.4a.9.9 0 1 0 0-1.8.9.9 0 0 0 0 1.8Z" />
        </svg>
      );
  }
}

const ETIQUETAS = [
  { nombre: "Centro", x: 61.5, y: 22.5 },
  { nombre: "Norte", x: 76.5, y: 29 },
  { nombre: "Sur", x: 90.0, y: 47 },
  { nombre: "Este", x: 54.5, y: 54 },
  { nombre: "Oeste", x: 79.0, y: 66.5 },
];

const TARJETAS_MOB = [
  { icon: "datos", title: "Qué datos usa", desc: "Indicadores de empleabilidad, salud mental y movilidad" },
  { icon: "chat", title: "Qué podés preguntar", desc: "Preguntas en lenguaje natural sobre el territorio" },
  { icon: "barras", title: "Qué devuelve", desc: "Datos, fuentes citadas y una recomendación" },
];

const TARJETAS_DESK = [
  { icon: "datos", title: "Qué datos usa", desc: "Empleabilidad, salud mental, movilidad OD y datos socioeconómicos.", link: "Ver fuentes" },
  { icon: "chat", title: "Qué podés preguntar", desc: "¿Dónde hay brechas de empleabilidad y mala cobertura? — en lenguaje natural.", link: "Ver ejemplos" },
  { icon: "barras", title: "Qué devuelve", desc: "Respuesta con datos, fuente, recomendación y zona resaltada.", link: "Ver ejemplo" },
];

function MobileLayout() {
  const navigate = useNavigate();
  return (
    <div
      className="relative flex h-screen flex-col overflow-hidden bg-background md:hidden"
      style={{ fontFamily: 'var(--font-sans)' }}
    >
      <img
        src={heroImage}
        alt=""
        className="absolute inset-0 h-full w-full object-cover"
        style={{ objectPosition: "64% 32%" }}
      />

      <div
        className="absolute inset-0"
        style={{
          background:
            "linear-gradient(180deg, var(--background) 0%, rgba(242,243,241,0.82) 26%, rgba(242,243,241,0.40) 44%, rgba(242,243,241,0.78) 62%, var(--background) 80%)",
        }}
      />

      <header className="relative flex flex-none items-center gap-[9px] px-[18px] pt-2">
        <span
          className="flex h-7 w-7 flex-none items-center justify-center rounded-[7px] bg-primary text-[11px] font-semibold text-primary-foreground"
          style={{ fontFamily: 'var(--font-mono)' }}
        >
          App BiT
        </span>
        <span className="text-[14px] font-bold text-foreground">Panel de Datos Públicos</span>
        <span className="ml-auto text-primary flex-none"><Icon name="ayuda" size={17} /></span>
      </header>

      <div className="relative flex-none px-[18px] pt-[22px]">
        <h1 className="mb-3 text-[29px] font-bold leading-[1.16] tracking-[-0.02em] text-foreground">
          Decidí dónde invertir primero, con datos del territorio
        </h1>
        <p className="text-[13.5px] leading-[1.55] text-muted-foreground" style={{ maxWidth: 320 }}>
          App BiT cruza empleabilidad, salud mental y movilidad para identificar
          regiones con riesgo de exclusión.
        </p>
      </div>

      <div className="relative flex-1" />

      <div className="relative flex-none px-[18px] flex flex-col gap-3">
        <Button variant="default" size="lg" className="!h-12 !text-[14.5px] w-full"
          onClick={() => navigate('/panel')}>
          <Icon name="mapa" className="mr-1" />
          Abrir mapa inteligente <span aria-hidden="true">→</span>
        </Button>
        <Button variant="secondary" size="lg" className="!h-11 !text-[14px] w-full"
          onClick={() => navigate('/metodologia')}>
          <Icon name="doc" className="mr-1" />
          Ver metodología
        </Button>
      </div>

      <div className="relative flex-none px-[18px] pt-4 flex flex-col gap-2">
        {TARJETAS_MOB.map((t) => (
          <Card key={t.title} size="sm">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Icon name={t.icon} size={16} className="text-primary" />
                {t.title}
              </CardTitle>
              <CardDescription>{t.desc}</CardDescription>
            </CardHeader>
          </Card>
        ))}
      </div>

      <footer className="relative flex-none flex items-center justify-center gap-[7px] px-[18px] py-[14px]">
        <span className="text-primary inline-flex"><Icon name="escudo" size={13} /></span>
        <span className="text-[11.5px] font-medium text-muted-foreground">Datos agregados y anonimizados</span>
        <span className="text-[10px] text-muted-foreground" style={{ fontFamily: 'var(--font-mono)' }}>
          · Región Centro
        </span>
      </footer>
    </div>
  );
}

function DesktopLayout() {
  const navigate = useNavigate();
  return (
    <div
      className="relative hidden min-h-screen w-full flex-col overflow-hidden bg-background md:flex"
      style={{ fontFamily: 'var(--font-sans)' }}
    >
      <img
        src={heroImage}
        alt=""
        className="absolute inset-0 h-full w-full object-cover"
        style={{ objectPosition: "68% 38%" }}
      />
      <div className="absolute inset-0" style={{
        background: "linear-gradient(92deg, var(--background) 0%, var(--background) 26%, rgba(242,243,241,0.72) 46%, rgba(242,243,241,0.18) 64%, transparent 80%)",
      }}/>
      <div className="absolute inset-0" style={{
        background: "linear-gradient(180deg, rgba(242,243,241,0.78) 0%, transparent 18%, transparent 58%, rgba(242,243,241,0.86) 86%, var(--background) 100%)",
      }}/>

      {ETIQUETAS.map((e) => (
        <span
          key={e.nombre}
          className="absolute -translate-x-1/2 -translate-y-1/2 whitespace-nowrap text-[12.5px] font-semibold text-foreground"
          style={{
            left: `${e.x}%`, top: `${e.y}%`,
            textShadow: "0 0 6px rgba(255,255,255,0.9), 0 1px 3px rgba(255,255,255,0.85), 0 0 14px rgba(255,255,255,0.7)",
          }}
        >{e.nombre}</span>
      ))}

      <div className="relative z-10 flex flex-1 flex-col">
        <header className="flex items-center gap-[11px] px-12 pt-[18px]">
          <span
            className="flex h-[34px] w-[34px] flex-none items-center justify-center rounded-[9px] bg-primary text-[13px] font-semibold text-primary-foreground"
            style={{ fontFamily: 'var(--font-mono)' }}
          >
            App BiT
          </span>
          <span className="text-[15.5px] font-bold text-foreground">Panel de Datos Públicos</span>
          <a className="ml-auto inline-flex cursor-pointer items-center gap-[7px] text-[13.5px] font-semibold text-primary no-underline"
            href="/metodologia">
            <Icon name="ayuda" size={16} /> ¿Cómo funciona?
          </a>
        </header>

        <div className="flex flex-1 flex-col justify-center px-12" style={{ maxWidth: 660 }}>
          <h1 className="text-[46px] font-bold leading-[1.12] tracking-[-0.025em] text-foreground" style={{ margin: "0 0 18px" }}>
            Decidí dónde<br/>invertir primero,<br/>con datos del territorio
          </h1>
          <p className="text-[15.5px] leading-[1.6] text-muted-foreground" style={{ margin: "0 0 28px", maxWidth: 440 }}>
            App BiT cruza empleabilidad, salud mental y movilidad para identificar
            regiones con riesgo de exclusión — y responde tus preguntas con datos, fuentes
            y una recomendación.
          </p>
          <div className="flex gap-3">
            <Button variant="default" size="lg" className="!h-[46px] !px-5 !text-[15px]"
              onClick={() => navigate('/panel')}>
              <Icon name="mapa" className="mr-1" />
              Abrir mapa inteligente <span aria-hidden="true" style={{ fontSize: 16 }}>→</span>
            </Button>
            <Button variant="secondary" size="lg" className="!h-[46px] !px-5 !text-[15px]"
              onClick={() => navigate('/metodologia')}>
              <Icon name="doc" className="mr-1" />
              Ver metodología
            </Button>
          </div>
        </div>

        <div className="grid grid-cols-3 gap-3.5 px-12 py-9">
          {TARJETAS_DESK.map((t) => (
            <div
              key={t.title}
              className="flex items-start gap-3.5 rounded-[10px] border border-border px-[18px] py-4"
              style={{ background: "rgba(255,255,255,0.96)", boxShadow: "0 3px 10px rgba(20,30,35,0.09),0 1px 2px rgba(20,30,35,0.07)" }}
            >
              <span className="flex h-11 w-11 flex-none items-center justify-center rounded-full bg-muted text-primary">
                <Icon name={t.icon} size={19} />
              </span>
              <div className="flex flex-col gap-1">
                <span className="text-sm font-bold text-foreground">{t.title}</span>
                <span className="text-[13px] leading-relaxed text-muted-foreground">{t.desc}</span>
                <a className="mt-0.5 cursor-pointer text-[12.5px] font-semibold text-primary no-underline">{t.link} ›</a>
              </div>
            </div>
          ))}
        </div>

        <footer className="flex items-center justify-center gap-2 px-12 pb-[18px]">
          <span className="text-primary inline-flex"><Icon name="escudo" size={14} /></span>
          <span className="text-xs font-medium text-muted-foreground">Datos agregados y anonimizados</span>
          <span className="text-[11px] text-muted-foreground" style={{ fontFamily: 'var(--font-mono)' }}>
            · Empleabilidad · Salud Mental · Movilidad · Región Centro
          </span>
        </footer>
      </div>
    </div>
  );
}

export default function LandingPage() {
  return (
    <>
      <MobileLayout />
      <DesktopLayout />
    </>
  );
}
