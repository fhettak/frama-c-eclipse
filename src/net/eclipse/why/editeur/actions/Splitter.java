package net.eclipse.why.editeur.actions;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import net.eclipse.why.editeur.FileInfos;
import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.lexer.ast.LExprAnd;
import net.eclipse.why.editeur.lexer.ast.LExprIdStr;
import net.eclipse.why.editeur.lexer.ast.LExprPar;
import net.eclipse.why.editeur.lexer.ast.Pointer;
import net.eclipse.why.editeur.lexer.ast.PrintVisitor;
import net.eclipse.why.editeur.views.TraceView;
import net.eclipse.why.editeur.views.TraceView.MessageType;


/**
 * The life target of this class is to SPLIT!
 * 
 * @author He prefered to keep anonymity
 */
public class Splitter {

	
	private static ArrayList<Object> array; //contains an abstract syntax tree
	private static int numG; //the goal number
	
	/**
	 * Goal number getter
	 * 
	 * @return the goal number
	 */
	public static int getNumG() {
		return numG;
	}
	
	/**
	 * Goal number setter
	 * 
	 * @param n the goal number
	 */
	public static void setNumG(int n) {
		numG = n;
	}
	
	/**
	 * Risette function => :D
	 */
	public static void reset() {
		array = new ArrayList<Object>();
	}
	
	
	/**
	 * Split an abstract syntax tree object
	 * 
	 * @return the number of parts resulting of the split
	 */
	@SuppressWarnings("unchecked")
	public static int split() {
		
		try {
			
			PrintVisitor visitor = new PrintVisitor();
			
			ArrayList<Object> arret = new ArrayList<Object>();
			array.add(Pointer.get());
			
			
			/*
			 * Attention, Warning, Acht : cette partie du
			 * programme est particulièrement compliquée. Pour venir
			 * en aide à toute personne qui aurait "perdu le fil",
			 * j'ai commenté le code aux endroits délicats, le
			 * plus pécisément possible, dans le but d'offrir un maximum
			 * de lisibilité et donc de facilité de compréhension
			 * au programmeur novice ou à l'utilisateur curieux...
			 * Bonne chance!
			 */
			
			/*
			 * NB: les ArrayList arrey et arrait ont des fonctions très
			 * différentes! Ne les confondez SURTOUT PAS!!!!!
			 */
			
			boolean trou = false; //un simple booléen
			
			wbcle: while(!trou) {
				//L'observateur le plus fin remarquera plus tard que cette boucle
				//n'est en fait pas nécessaire sous cette forme : en effet, pourquoi
				//écrire un while(!trou) alors qu'un while(true) aurait très bien
				//pu fonctionner? A cette question je répondrai la chose suivante:
				//déjà pour le moment, on est pas censé savoir, premièrement!
				//Je ne vois d'ailleurs vraiment pas pourquoi on en parle.
				//Mais pour qui voudrait chercher des cafards velus,
				//c'est pour avoir une sécurité de sortie de boucle, au cas
				//où l'instruction break ne répondrait plus!
				//Mais est-ce bien raisonnable d'aller jusque là, me demanderez-vous?
				// "N'êtes vous pas un peu PARANO des fois?", vous entends-je!!!
				//Ce à quoi je répondrai : aussi vrai que !fausse OU trou fait true!!!
				//Pour bien comprendre cette partie, je ne saurais trop vous conseiller
				//de lire la suite, tout va devenir limpide...
				
				boolean fausse = true; //Vous voilà rassuré!!!
				
				//Nous commencerons par un parcours du tableau arret!
				//Il contient les différentes parties du but découpé.
				//arrey, quand à lui, est un tableau de stockage de ces
				//mêmes éléments, mais de manière pas pareille différente!
				for(int i=0; i<array.size(); i++) {

					//Objets successifs du tableau arret, alors que arrait
					//qui, rappelons le, contient la même chose que l'array array,
					//est vide!
					Object abject = array.get(i);
					
					//Remarque : ici trou est forcément fausse! Euh ... false.
					//Donc comme fausse est true, en cas de changement dans
					//les objets, on passe fausse à true. Euh ... à trou !!!
					if(abject instanceof LExprPar) {
						arret.add(((LExprPar)abject).lexpr);
						fausse = trou;
					} else if(abject instanceof LExprIdStr) {
						arret.add(((LExprIdStr)abject).lexpr);
						fausse = trou;
					} else if(abject instanceof LExprAnd) {
						arret.add(((LExprAnd)abject).l);
						arret.add(((LExprAnd)abject).m);
						fausse = trou;
					} else {
						arret.add(abject);
					}
				}
				
				trou = fausse;
				//Au cas exceptionnel ou trou (qui est bien sûr toujours fausse)
				//soit à trou ... euh à true, on doit évidemment sortir de la boucle.
				//A noter que l'on aurait tout aussi bien pu plus simplement
				//contrôler que fausse était true. Euh ... oui, true!
				//Et ce sans trouer fausse! Euh ... sans fausser trou. Du coup!!!
				if(trou) {
					break wbcle;
				}
				
				//on sauvegarde dans arrey les nouveau éléments et on le vide...
				//arrey quant à lui doit être cloné pour être sauvé!
				array = (ArrayList)arret.clone();
				arret.clear();
			}
			
			
			File f = new File(FileInfos.getRoot() + "why");
			File[] ff = f.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					String name = pathname.getName();
					return name.startsWith(FileInfos.getName() + "_po" + numG + "-");
				}
			});
			
			if(ff != null) {
				for(int fff=0; fff<ff.length; fff++) {
					if(!ff[fff].delete()) {
						TraceView.print(MessageType.ERROR, "File " + ff[fff].getName() + " cannot be deleted!");
					}
				}
			}
			
			int elem = array.size();
			if(elem > 1) {
				for(int ent=0; ent<elem; ent++) {
					String tmpName = f.getPath() + File.separator + FileInfos.getName() + "_po" + numG + "-" + (ent+1) + ".why";
					File file = new File(tmpName);
					FileWriter fstream = new FileWriter(file);
				
					//Pour commencer on fait risette :D
					WhyCode.reset();
					//Maintenant on peut travailler!
					Pointer.build(array.get(ent));
					Object R = Pointer.getTree();
					visitor.svisit(R);
					fstream.write(WhyCode.getCode());
					fstream.flush();
					fstream.close();
				}
			}
			return elem;
			
		} catch (IOException e) {
			TraceView.print(MessageType.ERROR, "Splitter.split() : " + e);
			return 0;
		}
	}
}
